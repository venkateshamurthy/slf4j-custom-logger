package com.github.venkateshamurthy.util.logging;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.collect.ImmutableMap;

public class ToLevelledStringStyle extends ToStringStyle {
	
	public static final ToStringStyle mediumStyle =  new ToLevelledStringStyle(ToStringStyle.SIMPLE_STYLE,LevelOfDetail.MEDIUM){
		{
			this.setUseClassName(false);
            this.setUseIdentityHashCode(false);
		}
	};
	public static final ToStringStyle briefStyle =  new ToLevelledStringStyle(ToStringStyle.SIMPLE_STYLE,LevelOfDetail.BRIEF){
		{
			this.setUseClassName(false);
            this.setUseIdentityHashCode(false);
		}
	};;
	public static final Map<LevelOfDetail, ToStringStyle> mapLevelToStringStyle =
			ImmutableMap.of(LevelOfDetail.BRIEF,briefStyle,LevelOfDetail.MEDIUM,mediumStyle);

	/**
	 * 
	 */
	private static final long serialVersionUID = 2468189221607218130L;

	private final LevelOfDetail level;
	private final ToStringStyle style;

	/**
	 * @param level
	 */
	public ToLevelledStringStyle(ToStringStyle style, LevelOfDetail level) {
		super();
		this.style = style;
		this.level = level;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void append(final StringBuffer buffer, final String fieldName, Object value, Boolean fullDetail) {
		if (value != null && value instanceof LevelledToString) {
			style.append(buffer, fieldName,	((LevelledToString) value).toString(level), fullDetail);
		} else if (value != null && value.getClass().isArray()) {
			append(buffer, fieldName,(Object[])value,fullDetail);
		} else if (value != null && value instanceof Collection<?>) {
			for (Object obj : (Collection<?>) value){
				append(buffer, fieldName,obj,fullDetail);
			}
		} else {
			style.append(buffer, fieldName, value, fullDetail);
		}
	}

	@Override
	public void append(final StringBuffer buffer, final String fieldName,
			Object[] value, Boolean fullDetail) {
		if (value != null
				&& (value.getClass().isArray() && 
						LevelledToString.class.isAssignableFrom(value.getClass().getComponentType()))) {
			for (Object obj : value)
				style.append(buffer, fieldName,
						((LevelledToString) obj).toString(level), fullDetail);
		} else {
			style.append(buffer, fieldName, value, fullDetail);
		}
	}
}
