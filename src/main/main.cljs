(ns main
  {:clj-kondo/config '{:lint-as {applied-science.js-interop/let clojure.core/let}}}
  (:require ["pixi.js" :as PIXI]
            [applied-science.js-interop :as j]))

(defn render [delta ^js app ^js chest]
  (when (.. chest -isRotating)
    (j/update! chest :rotation (fn [r] (+ r (* 0.2 delta))))))

(defn on-click [^js chest]
  (j/update! chest :isRotating not))

(defn init []
  (println "Hello Shadow")
  (set! PIXI/BaseTexture.defaultOptions.scaleMode PIXI/SCALE_MODES.NEAREST)
  (j/let [^js {{:keys [width height]} :screen
               :as app} (PIXI/Application. (clj->js {:background "#1099bb"
                                                     :resolution js/devicePixelRatio}))

          chest-texture (PIXI/Texture.from "/sprites/chest_golden_closed.png")
          chest (PIXI/Sprite. chest-texture)
          render-fn (fn [delta] (render delta app chest))]
    (js/document.body.appendChild app.view)
    (.. chest -anchor (set 0.5))
    (-> chest
        (j/assoc! :width 100)
        (j/assoc! :height 100)
        (j/assoc! :buttonMode true)
        (j/assoc! :eventMode "static")
        (j/assoc! :x (/ width 2))
        (j/assoc! :y (/ height 2)))
    (.. chest (on "pointerdown" (fn [] (on-click chest))))
    (.. app -stage (addChild chest))
    (.. app -ticker (add render-fn))))

(comment
  (clj->js {:background "#1099bb"
            :resize js/window
            :resolution js/devicePixelRatio})
  (init))