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
  (is (= "Kapp                      Jason                     M               green                12/12/1912   "
         (render/render-record test-rec))))


(deftest test-render-records
  (let [title "Donuts"
        rendered (with-out-str (render/render-records title [test-rec test-rec]))
        [blank0 title-line header blank1 r0 r1 :as lines] (string/split-lines rendered)]
    (is (= 6 (count lines)))
    (is (empty? blank0))
    (is (= (str title ":") title-line))
    (is (= header "Last Name                 First Name                Gender          Favorite Color       Date of Birth"))
    (is (empty? blank1))
    (is (= r0 (render/render-record test-rec)))
    (is (= r1 (render/render-record test-rec)))))
