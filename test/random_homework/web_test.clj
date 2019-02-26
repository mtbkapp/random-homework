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
  (tr/compare-recs recs
                   (map #(update % :date-of-birth parse/parse-date)
                        (json/parse-string body true))))


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


(defn build-post
  [body]
  {:request-method :post 
   :uri "/"
   :body body})


(defn check-post-resp
  [{:keys [status] :as response} db expected-rec]
  (is (contains? @db expected-rec))
  (is (= 200 status)))


(deftest test-post
  (let [db (db/new-db)
        handler (web/app db)]
    (testing "bad input"
      (= 400 (:status (handler (build-post "donuts are the best food!")))))
    (testing "comma"
      (check-post-resp
        (handler (build-post (parse/rec->line tr/rec-b ",")))
        db tr/rec-b))
    (testing "pipe"
      (check-post-resp
        (handler (build-post (parse/rec->line tr/rec-c "|")))
        db tr/rec-c))
    (testing "space"
      (check-post-resp
        (handler (build-post (parse/rec->line tr/rec-a " ")))
        db tr/rec-a))))


(deftest test-web-api
  (let [db (db/new-db)
        handler (web/app db)]
    (testing "add all test records"
      (doseq [r tr/recs]
        (is (= 200 (-> (parse/rec->line r "|")
                       (build-post)
                       (handler)
                       (:status))))))
    (testing "sort by gender"
      (body-has-recs 
        (handler (build-get "/records/gender"))
        [tr/rec-c tr/rec-d tr/rec-a tr/rec-b]))))


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

