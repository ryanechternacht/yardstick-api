(ns yardstick-api.data.supports
  (:require [honeysql.helpers :refer [select from merge-join merge-where order-by]]
            [yardstick-api.db :as db]))

(def ^:private base-supports-query
  (-> (select :support.id :support.overview_title :support.overview_description
              :support.overview_action :support.details_title :support.details_subtitle
              :support.details_description)
      (from :support)
      (merge-join :student_support [:= :support.id :student_support.support_id])
      (order-by :student_support.ordering)))

(defn support-row->obj [{:keys [id overview_title overview_description overview_action
                                details_title details_subtitle details_description]}]
  {:id id
   :overview {:title overview_title
              :description overview_description
              :action overview_action}
   :details {:title details_title
             :subtitle details_subtitle
             :description details_description}})

(def ^:private base-support-tags-query
  (-> (select :support_tag.tag :support_support_tag.support_id)
      (from :support_tag)
      (merge-join :support_support_tag [:= :support_tag.id :support_support_tag.support_tag_id])
      (order-by :support_support_tag.ordering)))

(defn tag-row->obj [{:keys [tag]}]
  {:text tag})

(def ^:private base-support-steps-query
  (-> (select :support_step.title :support_step.step :support_support_step.support_id)
      (from :support_step)
      (merge-join :support_support_step [:= :support_step.id :support_support_step.support_step_id])
      (order-by :support_support_step.ordering)))

(defn step-row->obj [{:keys [title step]}]
  {:title title
   :text step})

(defn render-supports "attach tags and steps to raw support records"
  [db supports]
  (let [support-ids (map :id supports)
        support-tags (->> (-> base-support-tags-query
                              (merge-where [:in :support_support_tag.support_id support-ids]))
                          (db/execute db))
        support-steps (->> (-> base-support-steps-query
                               (merge-where [:in :support_support_step.support_id support-ids]))
                           (db/execute db))]
    (->> supports
         (map support-row->obj)
         (map (fn [{:keys [id] :as s}]
                (let [tags (->> support-tags
                                (filter #(= (:support_id %) id))
                                (map tag-row->obj))]
                  (assoc-in s [:overview :tags] tags))))
         (map (fn [{:keys [id] :as s}]
                (let [steps (->> support-steps
                                 (filter #(= (:support_id %) id))
                                 (map step-row->obj))]
                  (assoc-in s [:details :steps] steps)))))))

(defn get-by-student-id [db student-id]
  (let [supports
        (->>
         (-> base-supports-query
             (merge-where [:= :student_support.student_id student-id]))
         (db/execute db))]
    (render-supports db supports)))
