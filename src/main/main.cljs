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
    (j/assoc! (.. app -renderer -view -style)
              :width  (str innerWidth  "px")
              :height (str innerHeight "px"))
    (. js/window (scrollTo 0 0))
    (.. app -renderer (resize w h))
    (j/assoc! app
              :app-width w
              :app-height h)))

(defn construct-bar [^js graphic]
  (j/let [^js {:keys [length]} graphic]
    (doto graphic
      (. beginFill "0x86DCC1")
      (. lineStyle 10 "0x86DCC1")
      (. drawRect 0 0 length 4)
      (. endFill))))

#_{:clj-kondo/ignore [:unused-binding]}
(defn render [delta ^js app {:keys [exp-bar]}]
  (j/let [^js {:keys [app-width app-height]} app]
    (j/assoc! exp-bar
              :x (* app-width 0.375)
              :y (/ app-height 2.75)))
  (.. Group -shared update))

(defn on-bg-click [event ^js chest ^js exp-bar]
  #_(j/let [^js {{:keys [x y]} :global} event]
      (println "go to" x y)
      (.. (Tween. chest) (to (clj->js {:x x :y y}) 250) (start)))
  (j/update! exp-bar :length #(+ % 10))
  (.clear exp-bar)
  (construct-bar exp-bar))

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
          x-axis (Graphics.)
          y-axis (Graphics.)

          chest-texture (.. Texture (from "sprites/chest_golden_closed.png"))
          chest (Sprite. chest-texture)

          render-fn (fn [delta]
                      (render delta app {:exp-bar exp-bar}))]

    (j/assoc! bg
              :width app-width
              :height app-height
              :eventMode "static")
    (.. bg (on "pointerdown" (fn [evt] (on-bg-click evt chest exp-bar))))

    (j/assoc! exp-bar
              :length (* app-width 0.225)
              :x (/ app-width 2)
              :y (/ app-height 2))
    (construct-bar exp-bar)

    (j/assoc! x-axis
              :x 0
              :y (/ app-height 2))
    (doto x-axis
      (. beginFill "0x000000")
      (. lineStyle 5 "0x000000")
      (. drawRect 0 0 1000 0.2)
      (. endFill))

    (j/assoc! y-axis
              :x (/ app-width 2)
              :y (/ app-height 3)) ;; still confused with the coord here, what?
    (doto y-axis
      (. beginFill "0x000000")
      (. lineStyle 5 "0x000000")
      (. drawRect 0 0 0.2 1000)
      (. endFill))
    

    (.. chest -anchor (set 0.5))
    (j/assoc! chest
              :width 100
              :height 100
              :buttonMode true
              :eventMode "static"
              :x (/ app-width 2)
              :y (+ (/ app-height 2) 10))

    (.. chest (on "pointerdown" (fn [] (on-chest-click chest))))

    (.. app -stage (addChild bg))
    (.. app -stage (addChild exp-bar))
    (.. app -stage (addChild x-axis))
    (.. app -stage (addChild y-axis))
    (.. app -stage (addChild chest))

    (.. app -ticker (add render-fn))))

(comment
  (/ 2787 3)

  (clj->js {:background "#1099bb"
            :resize js/window
            :resolution js/devicePixelRatio})
  (init))