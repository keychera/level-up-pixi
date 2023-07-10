(ns main
  (:require ["pixi.js" :as PIXI]))

(defn render [delta ^js app ^js chest] 
  (set! (.. chest -x) (/ (.. app -screen -width) 2))
  (set! (.. chest -y) (/ (.. app -screen -height) 2))
  (let [rotation (.. chest -rotation)]
    (set! (.. chest -rotation) (+ rotation (* 0.1 delta)))))

(defn init []
  (println "Hello Shadow")
  (let [app (PIXI/Application. (clj->js {:background "#1099bb"
                                         :resolution js/devicePixelRatio}))
        chest (PIXI/Sprite.from "/sprites/chest_golden_closed.png")
        render-fn (fn [delta] (render delta app chest))]
    (js/document.body.appendChild app.view)
    (.. app -stage (addChild chest))
    (.. chest -anchor (set 0.5))
    (.. app -ticker (add render-fn))))


(comment

  (clj->js {:background "#1099bb"
            :resize js/window
            :resolution js/devicePixelRatio})
  (init))