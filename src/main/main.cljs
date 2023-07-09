(ns main
  (:require ["pixi.js" :as PIXI]))

(defonce app (atom nil))
(defonce bunny (atom nil))

(defn ^:dev/after-load render []
  (let [^js currently @app
        ^js bunny @bunny]
    (.. bunny -anchor (set 0.5))
    (set! (.. bunny -x) (/ (.. currently -screen -width) 2))
    (set! (.. bunny -y) (/ (.. currently -screen -height) 2))))

(defn init []
  (println "Hello Shadow")
  (let [initial-app (PIXI/Application.
                     (clj->js {:background "#1099bb" :width 640 :height 360}))
        bunny-sprite (PIXI/Sprite.from "https://pixijs.com/assets/bunny.png")]
    (reset! app initial-app)
    (reset! bunny bunny-sprite)
    (.. initial-app -stage (addChild bunny-sprite))
    (js/document.body.appendChild initial-app.view)
    (render)))


(comment
  (init))