(ns random-homework.test-recs
  (:require [clojure.test :refer :all]
            [random-homework.parse :as parse]))


(def rec-a {:gender "M"
            :first-name "JSON"
            :last-name "A"
            :fav-color "green"
            :date-of-birth (parse/parse-date "09/09/1909")})

(def rec-b {:gender "M"
            :first-name "JSON"
            :last-name "B"
            :fav-color "green"
            :date-of-birth (parse/parse-date "08/08/1808")})

(def rec-c {:gender "F"
            :first-name "JSON"
            :last-name "C"
            :fav-color "green"
            :date-of-birth (parse/parse-date "07/07/1707")})

(def rec-d {:gender "F"
            :first-name "JSON"
            :last-name "D"
            :fav-color "green"
            :date-of-birth (parse/parse-date "06/06/1606")})


(def recs [rec-a rec-c rec-b rec-d]) 


(defn compare-recs
  [expected actual]
  (let [ev (vec expected)
        av (vec actual)]
    (is (= (count ev) (count av)) "Equals lengths")
    (doseq [i (range (count ev))]
      (is (= (nth ev i)
             (nth av i))
          (str "index = " i)))))
