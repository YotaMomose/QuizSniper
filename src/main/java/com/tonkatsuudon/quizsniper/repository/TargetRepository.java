package com.tonkatsuudon.quizsniper.repository;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.tonkatsuudon.quizsniper.dao.QuizElementDao;
import com.tonkatsuudon.quizsniper.dao.TargetTemplateDao;
import com.tonkatsuudon.quizsniper.entity.TargetTemplates;
import com.tonkatsuudon.quizsniper.entity.TargetContents;
import com.tonkatsuudon.quizsniper.entity.Templates;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TargetRepository implements TargetTemplateDao, QuizElementDao {
    private final EntityManager entityManager;

    
    /**
     * ユーザーIDに紐づくターゲットテンプレートとその内容（TargetContents）を取得する。
     * リストの1番最初の要素としてセットしているテンプレートを取得する。
     * 
     * @param ユーザーID
     * @return ターゲットテンプレートのリスト
     */
    @Override
    public List<TargetTemplates> findTargetTemplates(String userId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TargetTemplates> query = builder.createQuery(TargetTemplates.class);
        Root<TargetTemplates> targetTemplatesRoot = query.from(TargetTemplates.class);
        
        Predicate userCondition = builder.equal(targetTemplatesRoot.get("userId"), userId);
        query.where(userCondition);
        query.orderBy(builder.desc(targetTemplatesRoot.get("isSet")));
        List<TargetTemplates> results = entityManager.createQuery(query).getResultList();
        return results;
        
    }

    /**
     * 引数で受け取ったidのTargetTemplatesのisSetをtrueに更新する
     * @param id
     */
    @Override
    public void setTargetTemplate(Integer id) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<TargetTemplates> query = cb.createQuery(TargetTemplates.class);
            Root<TargetTemplates> root = query.from(TargetTemplates.class);
            
            query.select(root).where(cb.equal(root.get("id"), id));

            TargetTemplates targetTemplate = entityManager.createQuery(query).getSingleResult();
            
            if (targetTemplate != null) {
                targetTemplate.setSet(true);
                entityManager.persist(targetTemplate); 
            }
        } catch (Exception e) {
            // TODO 結果が見つからない場合の処理（例: ログ出力など）
            System.out.println("TargetTemplate with ID " + id + " not found.");
            System.out.println(e);
        }

    }


    /**
     * 引数で受け取ったidのTargetTemplatesのisSetをfalseに更新する
     * @param id
     */
    @Override
    public void unsetTargetTemplate(Integer id) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<TargetTemplates> query = cb.createQuery(TargetTemplates.class);
            Root<TargetTemplates> root = query.from(TargetTemplates.class);
            
            query.select(root).where(cb.equal(root.get("id"), id));

            TargetTemplates targetTemplate = entityManager.createQuery(query).getSingleResult();
            
            if (targetTemplate != null) {
                targetTemplate.setSet(false);
                entityManager.persist(targetTemplate); 
            }
        } catch (Exception e) {
            // TODO 結果が見つからない場合の処理（例: ログ出力など）
            System.out.println("TargetTemplate with ID " + id + " not found.");
            System.out.println(e);
        }

    }

    /**
     * 引数で受け取ったcontentとtemplateidを持つTargetContentを追加する
     * @param content
     * @param templateId
     */
    @Override
    public void addTargetContent(String content, TargetTemplates setTemplate) {
        try {
            TargetContents targetContent = new TargetContents();
            targetContent.setContent(content);
            targetContent.setTargetTemplates(setTemplate);
            entityManager.persist(targetContent);
        } catch (Exception e) {
            // TODO: エラーハンドリング（例: ログ出力など）
            
            System.out.println(e);
        }
    }

    @Override
    public void delteSetContent(Integer id, Templates setTemplate) {
        try {
            
            // 検索結果を取得
            TargetTemplates targetTemplates = (TargetTemplates)setTemplate;
            List<TargetContents> targetContents = targetTemplates.getTargetContents();
            TargetContents deleteContents = targetContents.stream()
                .filter(content -> content.getId().equals(id))
                .findFirst()
                .orElse(null);
            
            // 検索したエンティティを削除
            if (deleteContents != null) {
                targetTemplates.getTargetContents().remove(deleteContents);
                entityManager.merge(targetTemplates);
            }
            
        } catch (Exception e) {
            // TODO: エラーハンドリング（例: ログ出力など）
            
            System.out.println(e);
        }
        
    }

    @Override
    /**
     * @param deleteIdList 削除するコンテンツのIDのリスト
     * @param Template コンテンツを削除する対象のテンプレート
     */
    public void bulkDeleteContents(List<Integer> deleteIdList, Templates Template) {
        try {
            // 検索結果を取得
            TargetTemplates targetTemplates = (TargetTemplates)Template;
            List<TargetContents> deleteContents = targetTemplates.getTargetContents().stream()
                    .filter(content -> deleteIdList.contains(content.getId()))
                    .collect(Collectors.toList());
            
            // 検索したエンティティを削除
            targetTemplates = entityManager.find(TargetTemplates.class, targetTemplates.getId());
            if (!deleteContents.isEmpty()) {
                targetTemplates.getTargetContents().removeAll(deleteContents);
                
                entityManager.merge(targetTemplates);
                entityManager.flush();
                
            }
            
        } catch (Exception e) {
            // TODO: エラーハンドリング（例: ログ出力など）
            
            System.out.println(e);
        }
        
    }
    
    /**
     * 引数で受け取ったcontentを編集対象のテンプレートに追加する
     * @param newContent　追加するコンテンツ
     * @param ediTemplate　追加対象のテンプレート
     */
    @Override
    public void addContent(String newContent, Templates ediTemplate) {
        try {
            TargetTemplates targetTemplates = (TargetTemplates)ediTemplate;
            TargetContents targetContent = new TargetContents();
            targetContent.setContent(newContent);
            targetContent.setTargetTemplates(targetTemplates);
            entityManager.persist(targetContent);
            targetTemplates.getTargetContents().add(targetContent);
        } catch (Exception e) {
            // TODO: エラーハンドリング（例: ログ出力など）
            
            System.out.println(e);
        }
    }

    /**
     * 引数で受け取ったIDに紐づくTargetTemplatesを取得する
     * @param id 取得対象のテンプレートのID
     * @return TargetTemplates 取得したテンプレート
     */
    @Override
    public Templates findTemplateById(Integer id) {
        try {
            TargetTemplates targetTemplate = entityManager.find(TargetTemplates.class, id);
            return targetTemplate;
        } catch (Exception e) {
            // TODO: エラーハンドリング（例: ログ出力など）
            System.out.println(e);
            return null;
        }
    }

    /**
     * 新たなテンプレートをDBに登録する
     * @param templateName 新規追加するテンプレートの名前
     * @param templateContents 新規追加するテンプレートのコンテンツ
     * @param userId ユーザーID
     */
    @Override
    public void addNewTemplate(String templateName,List<String> templateContents,String userId) {
        try {
            // 新しいジャンルテンプレートを作成
            TargetTemplates targetTemplate = new TargetTemplates();
            targetTemplate.setName(templateName);
            targetTemplate.setUserId(userId);
            targetTemplate.setTargetContents(new ArrayList<TargetContents>());

            // テンプレートを永続化
            entityManager.persist(targetTemplate);

            // コンテンツを作成して追加
            for (String content : templateContents) {
                TargetContents targetContent = new TargetContents();
                targetContent.setContent(content);
                targetContent.setTargetTemplates(targetTemplate);
                entityManager.persist(targetContent);
                targetTemplate.getTargetContents().add(targetContent);
            }
            entityManager.persist(targetTemplate);

        } catch (Exception e) {
            // TODO: エラーハンドリング（例: ログ出力など）
            System.out.println(e);
        }
    }

    /**
     * 引数のIDのテンプレートを削除する
     * @param id テンプレートのID
     */
    @Override
    public void deleteTemplate(Integer id) {
        try {
            // 削除対象のテンプレートを取得
            TargetTemplates template = entityManager.find(TargetTemplates.class, id);
            if (template != null) {
                // テンプレート自体を削除
                entityManager.remove(template);
            }
        } catch (Exception e) {
            // TODO: エラーハンドリング（例: ログ出力など）
            System.out.println(e);
        }
    }

    /**
     * 引数で受け取ったテンプレートを新規登録する
     * @param templates 登録するテンプレート
     * @param userId　テンプレートを紐づけるユーザー
     */
    @Override
    public void templateInitialSetup(Templates templates, String userId) {
        try {
            TargetTemplates targetTemplates = (TargetTemplates)templates;
            targetTemplates.setUserId(userId);
            targetTemplates.getTargetContents().forEach(content -> {
                content.setId(null);
                content.setTargetTemplates(targetTemplates);
            });
            
            entityManager.persist(targetTemplates);
        } catch (Exception e) {
            // TODO: エラーハンドリング（例: ログ出力など）
            
            System.out.println(e);
        }
    }
}
