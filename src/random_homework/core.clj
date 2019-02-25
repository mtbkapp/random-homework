(ns random-homework.core
  (:require [clojure.java.io :as io]
            [clojure.spec.alpha :as spec]
            [random-homework.parse :as parse]
            [random-homework.process :as process]
            [random-homework.render :as render])
  (:import [java.io File])
  (:gen-class))


(defn valid-file-path?
  [path]
  (let [file (io/file path)]
    (and (.exists file)
         (.isFile file))))


(def sort-by-fields #{"last-name" "date-of-birth" "gender"})


(spec/def ::cli-args
  (spec/cat :file (spec/and string? valid-file-path?)
            :delim (spec/cat :flag #{"--delim"}
                             :delim (into #{} (keys parse/delims)))
            :sort-by (spec/cat :flag #{"--sort-by"}
                               :field sort-by-fields)))


(defn parse-args
  [args]
  (let [{file :file {delim :delim} :delim {sortby :field} :sort-by} (spec/conform ::cli-args args)]
    {:op/file (io/file file)
     :op/delim (get parse/delims delim)
     :op/sort-by sortby}))


(spec/def :op/file #(instance? File %))
(spec/def :op/delim (into #{} (vals parse/delims)))
(spec/def :op/sort-by sort-by-fields)


(def usage "Usage: lein run <file-path> --delim <delim> --sort-by <sort-by>
           delim:   comma | space | pipe
           sort-by: last-name | date-of-birth | gender")


(defn -main
  [& args]
  (if (spec/valid? ::cli-args args)
    (let [{:keys [op/file op/delim op/sort-by]} (parse-args args)]
      (-> (parse/parse-file file delim)
          (process/sort-records sort-by)
          (render/render-records)))
    (do (println usage)
        (spec/explain ::cli-args args))))

