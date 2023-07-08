(ns main
  (:require ["pixi.js" :as PIXI]))

(defn init []
  (println "Hello Shadow")
  (let [app (PIXI/Application.
             (clj->js {:background "#1099bb" :width 640 :height 360}))] 
    (js/document.body.appendChild app.view)
    (let [bunny (PIXI/Sprite.from "https://pixijs.com/assets/bunny.png")]
      (.. app -stage (addChild bunny))
      (.. bunny -anchor (set 0.5))
      (set! (.. bunny -x) (/ (.. app -screen -width) 2))
      (set! (.. bunny -y) (/ (.. app -screen -height) 2)))))

(comment
  (init))