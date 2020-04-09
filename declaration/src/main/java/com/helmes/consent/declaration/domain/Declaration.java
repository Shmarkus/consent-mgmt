package com.helmes.consent.declaration.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "declaration")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Declaration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "declaration_sequence")
    @SequenceGenerator(name = "declaration_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "service_provider_id")
    private String serviceProviderId;

    @Column(name = "service_declaration_id")
    private String serviceDeclarationId;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description")
    private String description;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "technical_description")
    private String technicalDescription;

    @Column(name = "consent_max_duration_seconds")
    private Integer consentMaxDurationSeconds;

    @Column(name = "need_signature")
    private Boolean needSignature = false;

    @Column(name = "valid_until")
    private Long validUntil;

    @Column(name = "max_cache_seconds")
    private Integer maxCacheSeconds;
}


