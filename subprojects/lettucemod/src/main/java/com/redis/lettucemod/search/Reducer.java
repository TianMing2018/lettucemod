package com.redis.lettucemod.search;

import java.util.Optional;

import com.redis.lettucemod.protocol.SearchCommandKeyword;

public abstract class Reducer implements RediSearchArgument<Object, Object> {

	protected final Optional<String> as;

	protected Reducer(Optional<String> as) {
		this.as = as;
	}

	@Override
	public void build(SearchCommandArgs<Object, Object> args) {
		args.add(SearchCommandKeyword.REDUCE);
		buildFunction(args);
		as.ifPresent(a -> args.add(SearchCommandKeyword.AS).add(a));
	}

	protected String toString(String string) {
		StringBuilder builder = new StringBuilder(string);
		as.ifPresent(a -> builder.append(" AS ").append(a));
		return builder.toString();
	}

	protected abstract void buildFunction(SearchCommandArgs<Object, Object> args);

	@SuppressWarnings("unchecked")
	public static class Builder<B extends Builder<B>> {

		protected Optional<String> as = Optional.empty();

		public B as(String as) {
			this.as = Optional.of(as);
			return (B) this;
		}

	}

}
