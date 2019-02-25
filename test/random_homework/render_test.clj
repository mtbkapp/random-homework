(ns random-homework.render-test
  (:require [clojure.string :as string]
            [clojure.test :refer :all]
            [random-homework.parse :as parse]
            [random-homework.render :as render]))


(deftest test-render-field
  (testing "empty string"
    (is (= "    ") (render/render-field "" 4)))
  (testing "smaller than width"
    (is (= "Kapp      ") (render/render-field "Kapp" 10)))
  (testing "larger than width"
    (is (= "Kap" (render/render-field "Kapp" 3)))))


(def test-rec {:last-name "Kapp"
               :first-name "Jason"
               :gender "M"
               :fav-color "green"
               :date-of-birth (parse/parse-date "12/12/1912")})


(deftest test-render-record
  (is (= "Kapp                     Jason                    M              green               12/12/1912   "
         (render/render-record test-rec))))


(deftest test-render-records
  (let [rendered (with-out-str (render/render-records [test-rec test-rec]))
        [header blank r0 r1 :as lines] (string/split-lines rendered)]
    (is (= 4 (count lines)))
    (is (= header "Last Name                First Name               Gender         Favorite Color      Date of Birth"))
    (is (empty? blank))
    (is (= r0 (render/render-record test-rec)))
    (is (= r1 (render/render-record test-rec)))))
