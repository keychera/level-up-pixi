#!/usr/bin/env bb

(require
 '[clojure.string :as str]
 '[clojure.main :refer [demunge]]
 '[babashka.fs :as fs]
 '[babashka.process :refer [shell]])

(defn fn-name [a-fn]
  (-> a-fn str demunge (str/split #"@") first (str/replace #"babashka." "")))

(defn does [act & args]
  (println (str "[bb] " (fn-name act) " " (reduce #(str %1 " " %2) args)))
  (apply act args))

(def build "public")
(def remote-url (-> (shell {:out :string} "git config --get remote.origin.url") :out str/trim-newline))

(defn clean-build []
  (does fs/delete-tree (str build "/js"))
  (does shell "npx shadow-cljs release frontend"))

(defn publish-target []
  (does shell {:dir build :continue true} "git" "init")
  (does shell {:dir build} "git" "add" ".")
  (does shell {:dir build :continue true} "git" "commit" "-m" "\"Deploy to GitHub Pages\"")
  (does shell {:dir build} "git" "push" "--force" remote-url "main:gh-pages"))

(println "sending to" remote-url "=> gh-pages")
(clean-build)
(publish-target)
(prn "deployment done!")
