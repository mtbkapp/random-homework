(ns random-homework.web-test
  (:require [cheshire.core :as json]
            [clojure.spec.alpha :as spec]
            [clojure.test :refer :all]
            [random-homework.db :as db] 
            [random-homework.parse :as parse] 
            [random-homework.test-recs :as tr]
            [random-homework.web :as web]))


(def test-db (db/new-db tr/recs))


(deftest test-not-found
  (let [handler (web/app test-db)
        req {:request-method :get :uri "/unlimited/donuts"}
        resp (handler req)]
    (= 404 (:status resp))))


(defn build-get
  [uri]
  {:request-method :get :uri uri})


(defn body-has-recs
  [{:keys [status body] :as response} recs]
  (is (= 200 status))
  (is (= recs (map #(update % :date-of-birth parse/parse-date)
                   (json/parse-string body true)))))


(deftest test-gets
  (let [handler (web/app test-db)]
    (testing "sort by gender"
      (body-has-recs 
        (handler (build-get "/records/gender"))
        [tr/rec-c tr/rec-d tr/rec-a tr/rec-b]))
    (testing "sort by date of birth"
      (body-has-recs 
        (handler (build-get "/records/birthdate"))
        [tr/rec-d tr/rec-c tr/rec-b tr/rec-a]))
    (testing "sort by last name"
      (body-has-recs 
        (handler (build-get "/records/name"))
        [tr/rec-d tr/rec-c tr/rec-b tr/rec-a]))))


(deftest test-json-encode-middleware
  (let [handler (constantly {:body ["mmm" "donuts"]}) 
        wrapped-handler (web/json-encode-middleware handler)]
    (is (= {:body "[\"mmm\",\"donuts\"]"
            :headers {"Content-Type" "application/json"}}
           (wrapped-handler {})))))


(deftest test-inject-db
  (let [db "The db..."
        handler identity 
        wrapped-handler (web/inject-db handler db)]
    (is (= {:db db :mmm "donuts"}
           (wrapped-handler {:mmm "donuts"})))))


(deftest test-conform-port
  (is (= 8080 (web/conform-port "8080")))
  (is (= 1 (web/conform-port "1")))
  (is (= 65535 (web/conform-port "65535")))
  (is (= ::spec/invalid (web/conform-port "65536")))
  (is (= ::spec/invalid (web/conform-port "0")))
  (is (= ::spec/invalid (web/conform-port "donuts"))))

