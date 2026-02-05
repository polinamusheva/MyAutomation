package com.example.my_automation.entity;

import com.example.my_automation.utils.StringListConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.SourceType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "sources")
public class Source extends BaseDomainObject {

	private static final long serialVersionUID = 1L;

	@ColumnTransformer(read = "UPPER(name)", write = "UPPER(?)")
	@Column(name = "name")
	private String name;

	@Column(name = "enabled")
	private Boolean enabled;

	@Column(name = "source_type")
	@Enumerated(EnumType.STRING)
	private SourceType sourceType;

	@Column(name = "collection_sources")
	@Convert(converter = StringListConverter.class)
	private List<String> collectionSources;

	@Column(name = "category")
	private String category;

	public List<String> getCollectionSources() {
		if (collectionSources == null) {
			collectionSources = new ArrayList<>();
		}
		return collectionSources;
	}

	@Override
	public String toString() {
		return String.format("Source [name=%s, enabled=%s, sourceType=%s, category=%s]", name, enabled, sourceType, category);
	}

	@Override
	public int hashCode() {
		return Objects.hash(enabled, name, sourceType);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Source)) {
			return false;
		}
		Source other = (Source) obj;
		return Objects.equals(enabled, other.enabled) && Objects.equals(name, other.name)
				&& sourceType == other.sourceType;
	}

}
