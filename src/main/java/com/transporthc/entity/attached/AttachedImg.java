package com.transporthc.entity.attached;

import java.util.Date;

import com.transporthc.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "attached_imgs")
public class AttachedImg extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "reference_id")
    private String referenceId;

    @Column(name = "type")
    private Integer type;

    @Column(name = "img_path")
    private String imgPath;

    @Column(name = "uploaded_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date uploadedAt;
}
