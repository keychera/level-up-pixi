(ns main
  {:clj-kondo/config '{:lint-as {applied-science.js-interop/let clojure.core/let}}}
  (:require ["pixi.js" :as PIXI]
            [applied-science.js-interop :as j]))

(defonce noise [0.9224946357355075 0.195962217681654 0.823908454232499 0.21846578604175848 0.24790535608234543 0.12595059380269924 0.5293608423322345 0.7770868993048079 0.15261535648272484 0.7263776473945731])

(defn render [delta ^js app ^js chest]
  (j/let [amplitute 10
          ^js {:keys [original-x original-y]} chest]
    (if (.. chest -isShaking)
      (do (j/assoc! chest :x (+ original-x (* amplitute (rand-nth noise))))
          (j/assoc! chest :y (+ original-y (* amplitute (rand-nth noise)))))
      (do (j/assoc! chest :x original-x)
          (j/assoc! chest :y original-y)))))

(defn on-bg-click [event ^js chest]
  (j/let [^js {{:keys [x y]} :global} event]
    (j/assoc! chest :original-x x)
    (j/assoc! chest :original-y y)))

(defn on-chest-click [^js chest]
  (j/update! chest :isShaking not))

(defn init []
  (println "Hello Shadow")
  (set! PIXI/BaseTexture.defaultOptions.scaleMode PIXI/SCALE_MODES.NEAREST)
  (j/let [^js {{app-width :width
                app-height :height} :screen
               :as app} (PIXI/Application. (clj->js {:background "#1099bb"
                                                     :resolution js/devicePixelRatio}))

          chest-texture (PIXI/Texture.from "/sprites/chest_golden_closed.png")
          chest (PIXI/Sprite. chest-texture)
          bg (PIXI/Sprite. PIXI/Texture.WHITE)
          render-fn (fn [delta] (render delta app chest))]
    (js/document.body.appendChild app.view)
    (.. chest -anchor (set 0.5))
    (-> bg
        (j/assoc! :width app-width)
        (j/assoc! :height app-height)
        (j/assoc! :eventMode "static"))
    (-> chest
        (j/assoc! :width 100)
        (j/assoc! :height 100)
        (j/assoc! :buttonMode true)
        (j/assoc! :eventMode "static")
        (j/assoc! :original-x (/ app-width 2))
        (j/assoc! :x (/ app-width 2))
        (j/assoc! :original-y (/ app-height 2))
        (j/assoc! :y (/ app-height 2)))
    (.. bg (on "pointerdown" (fn [evt] (on-bg-click evt chest))))
    (.. chest (on "pointerdown" (fn [] (on-chest-click chest))))
    (.. app -stage (addChild bg))
    (.. app -stage (addChild chest))
    (.. app -ticker (add render-fn))))

(comment
  (clj->js {:background "#1099bb"
            :resize js/window
            :resolution js/devicePixelRatio})
  (init))