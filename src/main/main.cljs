(ns main
  {:clj-kondo/config '{:lint-as {applied-science.js-interop/let clojure.core/let}}}
  (:require ["pixi.js" :as PIXI :refer [BaseTexture Application Sprite Texture]]
            ["tweedle.js" :refer [Tween Group]]
            [applied-science.js-interop :as j]))

;; resize converted from https://github.com/pixijs/open-games/blob/main/puzzling-potions/src/main.ts
(defn resize [app]
  (j/let [min-w 375 min-h 700
          ^js {:keys [innerWidth innerHeight]} js/window
          scale-x (if (< innerWidth min-w) (/ min-w innerWidth) 1)
          scale-y (if (< innerHeight min-h) (/ min-h innerHeight) 1)
          scale (if (> scale-x scale-y) scale-x scale-y)
          w (* min-w scale)
          h (* min-h scale)]
    (j/assoc! app [:renderer :view :style :width] (str innerWidth "px"))
    (j/assoc! app [:renderer :view :style :height] (str innerHeight "px"))
    (. js/window (scrollTo 0 0))
    (.. app -renderer (resize w h)))
  app)

(defn render [delta ^js app ^js chest]
  (j/let [^js {:keys [original-x original-y
                      next-x next-y]} chest]
    (j/assoc! chest :x original-x)
    (j/assoc! chest :y original-y)
    (j/update! chest :original-x (fn [x] (+ x (* (- next-x original-x) 0.1 delta))))
    (j/update! chest :original-y (fn [y] (+ y (* (- next-y original-y) 0.1 delta)))))
  (.. Group -shared update))

(defn on-bg-click [event ^js chest]
  (j/let [^js {{:keys [x y]} :global} event]
    (j/assoc! chest :next-x x)
    (j/assoc! chest :next-y y)))

(defn on-chest-click [^js chest]
  (j/update! chest :isShaking not))

(defn init []
  (println "Hello Shadow")
  (set! (.. BaseTexture -defaultOptions -scaleMode) PIXI/SCALE_MODES.NEAREST)
  (j/let [^js {{app-width :width app-height :height} :screen :as app}
          (-> (Application. (clj->js {:background "#1099bb"
                                      :resolution (max js/window.devicePixelRatio 2)}))
              resize)

          chest-texture (.. Texture (from "sprites/chest_golden_closed.png"))
          chest (Sprite. chest-texture)
          _ (.. (Tween. (.-scale chest))
                (to (clj->js {:x 1.5 :y 1.5}) 1000)
                (repeat js/Infinity)
                (yoyo true)
                (start))

          bg (Sprite. PIXI/Texture.WHITE)

          render-fn (fn [delta] (render delta app chest))]
    (js/document.body.appendChild app.view)
    (js/window.addEventListener "resize" #(resize app))

    (-> bg
        (j/assoc! :width app-width)
        (j/assoc! :height app-height)
        (j/assoc! :eventMode "static"))
    (.. bg (on "pointerdown" (fn [evt] (on-bg-click evt chest))))

    (.. chest -anchor (set 0.5))
    (-> chest
        (j/assoc! :width 100)
        (j/assoc! :height 100)
        (j/assoc! :buttonMode true)
        (j/assoc! :eventMode "static")
        (j/assoc! :x (/ app-width 2))
        (j/assoc! :y (/ app-height 2))
        (j/assoc! :original-x (/ app-width 2))
        (j/assoc! :original-y (/ app-height 2))
        (j/assoc! :next-x (/ app-width 2))
        (j/assoc! :next-y (/ app-height 2)))
    (.. chest (on "pointerdown" (fn [] (on-chest-click chest))))

    (.. app -stage (addChild bg))
    (.. app -stage (addChild chest))
    (.. app -ticker (add render-fn))))

(comment
  (clj->js {:background "#1099bb"
            :resize js/window
            :resolution js/devicePixelRatio})
  (init))