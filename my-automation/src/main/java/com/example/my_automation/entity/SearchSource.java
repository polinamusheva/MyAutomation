package com.example.my_automation.entity;


import com.example.my_automation.dto.SearchSourceDTO;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@NamedNativeQuery(name = "SearchSource.findBySearchIdIn_Named",
	query = "SELECT ss.id, ss.search_id as searchId, s.id as sourceId, s.name as source, s.collection_sources as collectionSources FROM search_sources ss join sources s on ss.source_id = s.id where ss.search_id IN (:searchIds)",
	resultSetMapping = "com.webint.discovery.library.dto.search.BaseDomainDTO.PlainSearchSourceDTO")
@SqlResultSetMapping(name = "com.webint.discovery.library.dto.search.BaseDomainDTO.PlainSearchSourceDTO",
   classes = @ConstructorResult(targetClass = SearchSourceDTO.class,
                                columns = {@ColumnResult(name = "id", type = Long.class),
                                           @ColumnResult(name = "searchId", type = Long.class),
                                           @ColumnResult(name = "sourceId", type = Long.class),  
                                           @ColumnResult(name = "source", type = String.class),
                                           @ColumnResult(name = "collectionSources", type = String.class)}))
@Cacheable
@Entity
@Table(name = "search_sources", schema = "discovery")
public class SearchSource extends BaseDomainObject {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "source_id")
	private Source source;

	@ManyToOne(cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "search_id")
	private Search search;

	public Source getSource() {
		return source;
	}
	
	public void setSource(Source source) {
		this.source = source;
	}
	
	public Search getSearch() {
		return search;
	}
	
	public void setSearch(Search search) {
		this.search = search;
	}
}
