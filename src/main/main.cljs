(ns main
  (:require ["pixi.js" :as PIXI]))

(defn render [delta ^js app ^js chest] 
  (when (.. chest -isRotating)
    (let [rotation (.. chest -rotation)]
      (set! (.. chest -rotation) (+ rotation (* 0.1 delta))))))

(defn on-click [^js chest]
  (let [is-rotating? (.. chest -isRotating)]
    (set! (.. chest -isRotating) (not is-rotating?))))

(defn init []
  (println "Hello Shadow")
  (set! PIXI/BaseTexture.defaultOptions.scaleMode PIXI/SCALE_MODES.NEAREST)
  (let [app (PIXI/Application. (clj->js {:background "#1099bb"
                                         :resolution js/devicePixelRatio}))
        chest-texture (PIXI/Texture.from "/sprites/chest_golden_closed.png")
        chest (PIXI/Sprite. chest-texture)
        render-fn (fn [delta] (render delta app chest))]
    (js/document.body.appendChild app.view)
    (.. chest -anchor (set 0.5))
    (set! (.. chest -width) 100)
    (set! (.. chest -height) 100)
    (set! (.. chest -buttonMode) true)
    (set! (.. chest -eventMode) "static")
    (set! (.. chest -x) (/ (.. app -screen -width) 2))
    (set! (.. chest -y) (/ (.. app -screen -height) 2))
    (.. chest (on "pointerdown" (fn [] (on-click chest))))
    (js/console.log chest)
    (.. app -stage (addChild chest))
    (.. app -ticker (add render-fn))))

(comment
  (clj->js {:background "#1099bb"
            :resize js/window
            :resolution js/devicePixelRatio})
  (init))