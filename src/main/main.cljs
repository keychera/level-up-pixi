(ns main
  (:require ["pixi.js" :as PIXI]))

(defonce app (atom nil))

(defn ^:dev/after-load render []
  (let [^js currently @app
        bunny (PIXI/Sprite.from "https://pixijs.com/assets/bunny.png")]
    (.. currently -stage (addChild bunny))
    (.. bunny -anchor (set 0.5))
    (set! (.. bunny -x) (/ (.. currently -screen -width) 2))
    (set! (.. bunny -y) (/ (.. currently -screen -height) 2))))

(defn init []
  (println "Hello Shadow")
  (let [initial-app (PIXI/Application.
                     (clj->js {:background "#1099bb" :width 640 :height 360}))]
    (reset! app initial-app)
    (js/document.body.appendChild initial-app.view)
    (render)))


(comment
  (init))