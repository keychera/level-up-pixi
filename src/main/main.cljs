(ns main
  (:require ["pixi.js" :as PIXI]))

(defn render [delta ^js app ^js bunny]
  (.. bunny -anchor (set 0.5))
  (set! (.. bunny -x) (/ (.. app -screen -width) 2))
  (set! (.. bunny -y) (/ (.. app -screen -height) 2))
  (let [rotation (.. bunny -rotation)]
    (set! (.. bunny -rotation) (+ rotation (* 0.1 delta)))))

(defn init []
  (println "Hello Shadow")
  (let [app (PIXI/Application. (clj->js {:background "#1099bb" :width 640 :height 360}))
        bunny (PIXI/Sprite.from "https://pixijs.com/assets/bunny.png")
        render-fn (fn [delta] (render delta app bunny))]
    (.. app -stage (addChild bunny))
    (js/document.body.appendChild app.view)
    (.. app -ticker (add render-fn))))


(comment
  (init))