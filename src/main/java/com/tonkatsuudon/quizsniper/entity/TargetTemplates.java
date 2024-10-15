package com.tonkatsuudon.quizsniper.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "target_templates")
@Data
public class TargetTemplates implements Templates {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20)
    private String name;

    @Column(length = 20)
    private String userId;

    @Column
    private boolean isSet;

    //fetch = FetchType.EAGERとすることで一度のアクセスでtargetContentsまで取得する。デフォルトはFetchType.LAZY
    @OneToMany(mappedBy = "targetTemplates", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TargetContents> targetContents;

    /**
     * ログインしない場合のターゲットのデフォルトを返す
     * @param defaultTarget
     * @return 
     */
    public List<TargetTemplates> getDefaultTmplate(List<String> defaultTarget) {
        
        List<TargetContents> targetContents = new ArrayList<>();
        for(String target: defaultTarget) {
            TargetContents defaultContent = new TargetContents();
            defaultContent.setContent(target);
            targetContents.add(defaultContent);
        }
        
        TargetTemplates targetTemplates = new TargetTemplates();
        targetTemplates.setTargetContents(targetContents);
        targetTemplates.setName("テンプレート1");

        List<TargetTemplates> defaultTmp = new ArrayList<>();
        defaultTmp.add(targetTemplates);

        return defaultTmp;

    }
}

