(ns main)

(defn init []
  (println "Hello Shadow")
  (let [app (js/PIXI.Application.
             (clj->js {:background "#1099bb" :width 640 :height 360}))]
    (js/document.body.appendChild app.view)))

(comment
  (init))