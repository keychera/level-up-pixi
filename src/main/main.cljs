(ns main
  {:clj-kondo/config '{:lint-as {applied-science.js-interop/let clojure.core/let}}}
  (:require ["pixi.js" :as PIXI :refer [BaseTexture Application Graphics Sprite Texture]]
            ["tweedle.js" :refer [Tween Group]]
            [applied-science.js-interop :as j]))

;; resize converted from https://github.com/pixijs/open-games/blob/main/puzzling-potions/src/main.ts
(defn resize [app]
  (j/let [min-w 375 min-h 700
          ^js {:keys [innerWidth innerHeight]} js/window
          scale-x (if (< innerWidth min-w) (/ min-w innerWidth) 1)
          scale-y (if (< innerHeight min-h) (/ min-h innerHeight) 1)
          scale (if (> scale-x scale-y) scale-x scale-y)
          w (* innerWidth scale)
          h (* innerHeight scale)]
    (j/assoc-in! app [:renderer :view :style :width] (str innerWidth "px"))
    (j/assoc-in! app [:renderer :view :style :height] (str innerHeight "px"))
    (. js/window (scrollTo 0 0))
    (.. app -renderer (resize w h))
    (j/assoc! app
              :app-width w
              :app-height h)))

#_{:clj-kondo/ignore [:unused-binding]}
(defn render [delta ^js app {:keys [exp-bar]}]
  (j/let [^js {:keys [app-width app-height]} app]
    (j/assoc! exp-bar
              :x (* app-width 0.375)
              :y (/ app-height 2.75)))
  (.. Group -shared update))

(defn on-bg-click [event ^js chest]
  (j/let [^js {{:keys [x y]} :global} event]
    (println "go to" x y)
    (.. (Tween. chest) (to (clj->js {:x x :y y}) 250) (start))))

(defn on-chest-click [^js chest]
  (j/update! chest :isShaking not))

(defn init []
  (println "Hello Shadow")
  (set! (.. BaseTexture -defaultOptions -scaleMode) PIXI/SCALE_MODES.NEAREST)
  (j/let [^js app (Application. (clj->js {:background "#1099bb"
                                          :resolution (max js/window.devicePixelRatio 2)}))
          _ (js/document.body.appendChild (. app -view))
          _ (js/window.addEventListener "resize" #(resize app))
          ^js {:keys [app-width app-height]} (resize app)

          bg (Sprite. PIXI/Texture.WHITE)

          exp-bar (Graphics.)
          _ (doto exp-bar
              (. beginFill "0xFF00FF")
              (. lineStyle 10 "0xFF00FF")
              (. drawRect 0 0 (* app-width 0.225) 10)
              (. endFill))

          chest-texture (.. Texture (from "sprites/chest_golden_closed.png"))
          chest (Sprite. chest-texture)

          render-fn (fn [delta]
                      (render delta app {:exp-bar exp-bar}))]

    (-> bg
        (j/assoc! :width app-width)
        (j/assoc! :height app-height)
        (j/assoc! :eventMode "static"))
    (.. bg (on "pointerdown" (fn [evt] (on-bg-click evt chest))))

    (-> exp-bar
        (j/assoc! :x (/ app-width 2))
        (j/assoc! :y (/ app-height 2)))

    (.. chest -anchor (set 0.5))
    (-> chest
        (j/assoc! :width 100)
        (j/assoc! :height 100)
        (j/assoc! :buttonMode true)
        (j/assoc! :eventMode "static")
        (j/assoc! :x (/ app-width 2))
        (j/assoc! :y (/ app-height 2)))
    (.. chest (on "pointerdown" (fn [] (on-chest-click chest))))

    (.. app -stage (addChild bg))
    (.. app -stage (addChild exp-bar))
    (.. app -stage (addChild chest))

    (.. app -ticker (add render-fn))))

(comment
  (clj->js {:background "#1099bb"
            :resize js/window
            :resolution js/devicePixelRatio})
  (init))