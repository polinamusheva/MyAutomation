package com.example.my_automation.entity;


import com.example.my_automation.dto.SearchCollectedSourcesDTO;
import com.example.my_automation.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NamedNativeQuery(name = "Search.searchCollectedSources_Named",
query = "SELECT t.id, t.search_id as searchId, s.name FROM interior.projects t left outer join interior.clients r on t.client_id=r.id  " +
		" left outer join interior.sources s on r.source_id=s.id where t.type='search' and t.collected_data > 0  and t.search_id IN (:searchIds)",
resultSetMapping = "com.webint.discovery.library.dto.search.BaseDomainDTO.SearchCollectedSourcesDTO")
@SqlResultSetMapping(name = "com.webint.discovery.library.dto.search.BaseDomainDTO.SearchCollectedSourcesDTO",
classes = @ConstructorResult(targetClass = SearchCollectedSourcesDTO.class,
                            columns = {@ColumnResult(name = "id", type = Long.class),
                                       @ColumnResult(name = "searchId", type = Long.class),
                                       @ColumnResult(name = "name", type = String.class)}))

@Cacheable
@Entity
@Table(name = "searches", schema = "interior")
@Getter
@Setter
public class Search extends BaseDomainObject {

	private static final long serialVersionUID = 1L;

	@Column(name = "name")
	private String name;

	@Column(name = "term")
	private String term;

	@Enumerated(EnumType.STRING)
	@Column(name = "purpose")
	private String purpose;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private Status status;

	@Column(name = "created_at")
	private Date createdAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Search parent;

	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Search> children = new ArrayList<>();

	@OneToMany(mappedBy = "search", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<SearchSource> searchSources = new ArrayList<>();
	
	@Column(name = "enabled")
	private Boolean enabled;

	@Transient
	private int collectedRecords;

	@Transient
	public Long getIdOfParent() {
		if (getParent() != null) {
			return getParent().getId();
		}
		return null;
	}
	
	@Override
	public String toString() {
		return String.format("Search [id=%s, term=%s, purpose=%s, status=%s, "
				+ "createdAt=%s, user=%s, enabled=%s, imageUrl=%s]", id, term, purpose,
				status, createdAt, user, enabled);
	}
}