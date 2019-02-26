(ns random-homework.core
  (:require [clojure.java.io :as io]
            [clojure.spec.alpha :as spec]
            [random-homework.parse :as parse]
            [random-homework.process :as process]
            [random-homework.render :as render])
  (:gen-class))


(defn valid-file-path?
  "Predicate that determines if the given path references an existing file."
  [path]
  (let [file (io/file path)]
    (and (.exists file)
         (.isFile file))))


(spec/def ::cli-args
  (spec/cat :comma-file valid-file-path?
            :pipe-file valid-file-path?
            :space-file valid-file-path?))


(def usage "Usage: lein run <comma-file-path> <pipe-file-path> <space-file-path>")


(defn -main
  [& args]
  (if (spec/valid? ::cli-args args)
    (let [{:keys [comma-file pipe-file space-file]} (spec/conform ::cli-args args)
          records (process/combine-records (parse/parse-file comma-file ",")
                                           (parse/parse-file pipe-file "|")
                                           (parse/parse-file space-file " "))]
      (render/render-records 
        "Data sorted by gender" 
        (process/sort-records records :sort-by/gender))
      (render/render-records 
        "Data sorted by date of birth" 
        (process/sort-records records :sort-by/date-of-birth))
      (render/render-records
        "Data sorted by last name."
        (process/sort-records records :sort-by/last-name)))
    (do (println usage)
        (spec/explain ::cli-args args))))

