package com.redis.lettucemod.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalLong;

import com.redis.lettucemod.protocol.SearchCommandKeyword;

public class CreateOptions<K, V> implements RediSearchArgument<K, V> {

	public enum DataType {
		HASH, JSON
	}

	private Optional<DataType> on = Optional.empty();
	private final List<K> prefixes;
	private Optional<V> filter = Optional.empty();
	private Optional<Language> defaultLanguage = Optional.empty();
	private Optional<K> languageField = Optional.empty();
	private OptionalDouble defaultScore = OptionalDouble.empty();
	private Optional<K> scoreField = Optional.empty();
	private Optional<K> payloadField = Optional.empty();
	private boolean maxTextFields;
	private OptionalLong temporary = OptionalLong.empty();
	private boolean noOffsets;
	private boolean noHL;
	private boolean noFields;
	private boolean noFreqs;
	private boolean skipInitialScan;
	/**
	 * Set this to empty list for STOPWORDS 0
	 */
	private Optional<List<V>> stopWords = Optional.empty();

	private CreateOptions(Builder<K, V> builder) {
		this.on = builder.on;
		this.prefixes = builder.prefixes;
		this.filter = builder.filter;
		this.defaultLanguage = builder.defaultLanguage;
		this.languageField = builder.languageField;
		this.defaultScore = builder.defaultScore;
		this.scoreField = builder.scoreField;
		this.payloadField = builder.payloadField;
		this.maxTextFields = builder.maxTextFields;
		this.temporary = builder.temporary;
		this.noOffsets = builder.noOffsets;
		this.noHL = builder.noHL;
		this.noFields = builder.noFields;
		this.noFreqs = builder.noFreqs;
		this.skipInitialScan = builder.skipInitialScan;
		this.stopWords = builder.stopWords;
	}

	@Override
	public void build(SearchCommandArgs<K, V> args) {
		on.ifPresent(o -> args.add(SearchCommandKeyword.ON).add(o.name()));
		if (!prefixes.isEmpty()) {
			args.add(SearchCommandKeyword.PREFIX);
			args.add(prefixes.size());
			prefixes.forEach(args::addKey);
		}
		filter.ifPresent(f -> args.add(SearchCommandKeyword.FILTER).addValue(f));
		defaultLanguage.ifPresent(l -> args.add(SearchCommandKeyword.LANGUAGE).add(l.getId()));
		languageField.ifPresent(f -> args.add(SearchCommandKeyword.LANGUAGE_FIELD).addKey(f));
		defaultScore.ifPresent(s -> args.add(SearchCommandKeyword.SCORE).add(s));
		scoreField.ifPresent(f -> args.add(SearchCommandKeyword.SCORE_FIELD).addKey(f));
		payloadField.ifPresent(f -> args.add(SearchCommandKeyword.PAYLOAD_FIELD).addKey(f));
		if (maxTextFields) {
			args.add(SearchCommandKeyword.MAXTEXTFIELDS);
		}
		temporary.ifPresent(t -> args.add(SearchCommandKeyword.TEMPORARY).add(t));
		if (noOffsets) {
			args.add(SearchCommandKeyword.NOOFFSETS);
		}
		if (noHL) {
			args.add(SearchCommandKeyword.NOHL);
		}
		if (noFields) {
			args.add(SearchCommandKeyword.NOFIELDS);
		}
		if (noFreqs) {
			args.add(SearchCommandKeyword.NOFREQS);
		}
		if (skipInitialScan) {
			args.add(SearchCommandKeyword.SKIPINITIALSCAN);
		}
		stopWords.ifPresent(w -> {
			args.add(SearchCommandKeyword.STOPWORDS).add(w.size());
			w.forEach(args::addValue);
		});
	}

	@Override
	public String toString() {
		return "CreateOptions [on=" + on + ", prefixes=" + prefixes + ", filter=" + filter + ", defaultLanguage="
				+ defaultLanguage + ", languageField=" + languageField + ", defaultScore=" + defaultScore
				+ ", scoreField=" + scoreField + ", payloadField=" + payloadField + ", maxTextFields=" + maxTextFields
				+ ", temporary=" + temporary + ", noOffsets=" + noOffsets + ", noHL=" + noHL + ", noFields=" + noFields
				+ ", noFreqs=" + noFreqs + ", skipInitialScan=" + skipInitialScan + ", stopWords=" + stopWords + "]";
	}

	public static <K, V> Builder<K, V> builder() {
		return new Builder<>();
	}

	public static final class Builder<K, V> {

		private Optional<DataType> on = Optional.of(DataType.HASH);
		private final List<K> prefixes = new ArrayList<>();
		private Optional<V> filter = Optional.empty();
		private Optional<Language> defaultLanguage = Optional.empty();
		private Optional<K> languageField = Optional.empty();
		private OptionalDouble defaultScore = OptionalDouble.empty();
		private Optional<K> scoreField = Optional.empty();
		private Optional<K> payloadField = Optional.empty();
		private boolean maxTextFields;
		private OptionalLong temporary = OptionalLong.empty();
		private boolean noOffsets;
		private boolean noHL;
		private boolean noFields;
		private boolean noFreqs;
		private boolean skipInitialScan;
		private Optional<List<V>> stopWords = Optional.empty();

		public Builder<K, V> on(DataType on) {
			this.on = Optional.of(on);
			return this;
		}

		public Builder<K, V> prefix(K prefix) {
			this.prefixes.add(prefix);
			return this;
		}

		@SuppressWarnings("unchecked")
		public Builder<K, V> prefixes(K... prefixes) {
			this.prefixes.addAll(Arrays.asList(prefixes));
			return this;
		}

		public Builder<K, V> filter(V filter) {
			this.filter = Optional.of(filter);
			return this;
		}

		public Builder<K, V> defaultLanguage(Language defaultLanguage) {
			this.defaultLanguage = Optional.of(defaultLanguage);
			return this;
		}

		public Builder<K, V> languageField(K languageField) {
			this.languageField = Optional.of(languageField);
			return this;
		}

		public Builder<K, V> defaultScore(double defaultScore) {
			this.defaultScore = OptionalDouble.of(defaultScore);
			return this;
		}

		public Builder<K, V> scoreField(K scoreField) {
			this.scoreField = Optional.of(scoreField);
			return this;
		}

		public Builder<K, V> payloadField(K payloadField) {
			this.payloadField = Optional.of(payloadField);
			return this;
		}

		public Builder<K, V> maxTextFields(boolean maxTextFields) {
			this.maxTextFields = maxTextFields;
			return this;
		}

		public Builder<K, V> temporary(long temporary) {
			this.temporary = OptionalLong.of(temporary);
			return this;
		}

		public Builder<K, V> noOffsets(boolean noOffsets) {
			this.noOffsets = noOffsets;
			return this;
		}

		public Builder<K, V> noHL(boolean noHL) {
			this.noHL = noHL;
			return this;
		}

		public Builder<K, V> noFields(boolean noFields) {
			this.noFields = noFields;
			return this;
		}

		public Builder<K, V> noFreqs(boolean noFreqs) {
			this.noFreqs = noFreqs;
			return this;
		}

		public Builder<K, V> skipInitialScan(boolean skipInitialScan) {
			this.skipInitialScan = skipInitialScan;
			return this;
		}

		@SuppressWarnings("unchecked")
		public Builder<K, V> stopWords(V... stopWords) {
			this.stopWords = Optional.of(Arrays.asList(stopWords));
			return this;
		}

		public CreateOptions<K, V> build() {
			return new CreateOptions<>(this);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(defaultLanguage, defaultScore, filter, languageField, maxTextFields, noFields, noFreqs,
				noHL, noOffsets, on, payloadField, prefixes, scoreField, skipInitialScan, stopWords, temporary);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreateOptions<?, ?> other = (CreateOptions<?, ?>) obj;
		return Objects.equals(defaultLanguage, other.defaultLanguage)
				&& Objects.equals(defaultScore, other.defaultScore) && Objects.equals(filter, other.filter)
				&& Objects.equals(languageField, other.languageField) && maxTextFields == other.maxTextFields
				&& noFields == other.noFields && noFreqs == other.noFreqs && noHL == other.noHL
				&& noOffsets == other.noOffsets && Objects.equals(on, other.on)
				&& Objects.equals(payloadField, other.payloadField) && Objects.equals(prefixes, other.prefixes)
				&& Objects.equals(scoreField, other.scoreField) && skipInitialScan == other.skipInitialScan
				&& Objects.equals(stopWords, other.stopWords) && Objects.equals(temporary, other.temporary);
	}

}
