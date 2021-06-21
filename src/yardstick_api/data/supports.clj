(ns yardstick-api.data.supports
  (:require [honeysql.helpers :refer [select from merge-join merge-where order-by]]
            [yardstick-api.db :as db]
            [yardstick-api.data.language :as lang]))

(def ^:private base-supports-query
  (-> (select :support.id :support.overview_title_lang :support.overview_description_lang
              :support.overview_action_lang :support.details_title_lang
              :support.details_subtitle_lang :support.details_description_lang)
      (from :support)
      (merge-join :student_support [:= :support.id :student_support.support_id])
      (order-by :student_support.ordering)))

(defn- support-row->obj [{:keys [id overview_title_lang overview_description_lang
                                 overview_action_lang details_title_lang
                                 details_subtitle_lang details_description_lang]}]
  {:id id
   :overview {:title overview_title_lang
              :description overview_description_lang
              :action overview_action_lang}
   :details {:title details_title_lang
             :subtitle details_subtitle_lang
             :description details_description_lang}})

(def ^:private base-support-tags-query
  (-> (select :support_tag.tag_lang :support_support_tag.support_id)
      (from :support_tag)
      (merge-join :support_support_tag [:= :support_tag.id :support_support_tag.support_tag_id])
      (order-by :support_support_tag.ordering)))

(defn tag-row->obj [{:keys [tag_lang]}]
  {:text tag_lang})

(def ^:private base-support-steps-query
  (-> (select :support_step.title_lang :support_step.step_lang :support_support_step.support_id)
      (from :support_step)
      (merge-join :support_support_step [:= :support_step.id :support_support_step.support_step_id])
      (order-by :support_support_step.ordering)))

(defn step-row->obj [{:keys [title_lang step_lang]}]
  {:title title_lang
   :text step_lang})

(defn render-supports "attach tags and steps to raw support records"
  [db lang supports]
  (let [support-ids (map :id supports)
        support-tags (->> (-> base-support-tags-query
                              (merge-where [:in :support_support_tag.support_id support-ids]))
                          (db/execute db))
        support-steps (->> (-> base-support-steps-query
                               (merge-where [:in :support_support_step.support_id support-ids]))
                           (db/execute db))]
    (->> supports
         (lang/render-language db lang)
         (map support-row->obj)
         (map (fn [{:keys [id] :as s}]
                (let [tags (->> support-tags
                                (filter #(= (:support_id %) id))
                                (lang/render-language db lang)
                                (map tag-row->obj))]
                  (assoc-in s [:overview :tags] tags))))
         (map (fn [{:keys [id] :as s}]
                (let [steps (->> support-steps
                                 (filter #(= (:support_id %) id))
                                 (lang/render-language db lang)
                                 (map step-row->obj))]
                  (assoc-in s [:details :steps] steps)))))))

(defn get-by-student-id [db lang student-id]
  (->> (merge-where base-supports-query
                    [:= :student_support.student_id student-id])
       (db/execute db)
       (render-supports db lang)))
