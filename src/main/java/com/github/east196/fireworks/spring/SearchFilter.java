package com.github.east196.fireworks.spring;

import com.github.east196.boonplus.BoonPlus;
import com.github.east196.fireworks.Time;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import java.util.Map.Entry;


public class SearchFilter {

    public enum Operator {
        EQ,

        LIKE,

        GT,

        LT,

        GTE,

        LTE;
    }

    public String fieldName;

    public Object value;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Operator operator;

    public SearchFilter(final String fieldName, final Operator operator, final Object value) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
    }

    public static Map<String, SearchFilter> parse(final Map<String, Object> searchParams) {
        final HashMap<String, SearchFilter> filters = Maps.<String, SearchFilter>newHashMap();
        Set<Entry<String, Object>> _entrySet = searchParams.entrySet();
        for (final Entry<String, Object> entry : _entrySet) {
            final String key = entry.getKey();
            final Object value = entry.getValue();
            boolean _isNotBlank = StringUtils.isNotBlank(((String) value));
            if (_isNotBlank) {
                final String[] names = StringUtils.split(key, "_");
                int _length = names.length;
                boolean _notEquals = (_length != 2);
                if (_notEquals) {
                    throw new IllegalArgumentException((key + " is not a valid search filter name"));
                }
                final String filedName = names[1];
                String _get = names[0];
                final Operator operator = Operator.valueOf(_get);
                final SearchFilter filter = new SearchFilter(filedName, operator, value);
                filters.put(key, filter);
            }
        }
        return filters;
    }

    public static <T> List<SearchFilter> from(final Map<String, String[]> requestParameterMap,
                                              final Class<T> entityClass) {
        Map<String, String[]> parameterMap = new HashMap<String, String[]>();
        for (Entry<String, String[]> e : requestParameterMap.entrySet()) {
            // 筛选搜索条件
            if (!e.getKey().equals("_") && e.getKey().contains("_") && !e.getKey().contains("[_]")) {
                parameterMap.put(e.getKey(), e.getValue());
            }
        }

        // 获取类属性名称与类型
        final Map<String, Type> fieldTypes = new HashMap<>();
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            fieldTypes.put(field.getName().toLowerCase(), field.getGenericType());
        }

        List<SearchFilter> _xblockexpression = null;
        {
            Set<Entry<String, String[]>> _entrySet = parameterMap.entrySet();
            final Function<Entry<String, String[]>, SearchFilter> _function = new Function<Entry<String, String[]>, SearchFilter>() {
                @Override
                public SearchFilter apply(final Entry<String, String[]> entry) {
                    SearchFilter _xblockexpression = null;
                    {
                        SearchFilter filter = null;
                        final String key = entry.getKey();
                        String[] _value = entry.getValue();
                        String value = _value[0];
                        boolean _isNotBlank = StringUtils.isNotBlank(value);
                        if (_isNotBlank) {
                            final String[] names = StringUtils.split(key, "_");
                            int _length = names.length;
                            boolean _notEquals = (_length != 2);
                            if (_notEquals) {
                                throw new IllegalArgumentException((key + " is not a valid search filter name"));
                            }
                            Object object = value;
                            Type type = fieldTypes.get(names[0].toLowerCase());
                            if (null != type) {
                                if (type.toString().equals("class java.util.Date")) {
                                    object = Time.praseDate(value);
                                } else if (type.toString().equals("class java.lang.Boolean")) {
                                    object = Boolean.parseBoolean(value);
                                } else if (type.toString().equals("class java.lang.Integer") && !"null".equals(value)) {
                                    object = Integer.parseInt(value);
                                } else if (type.toString().equals("class java.lang.Double")) {
                                    object = Double.parseDouble(value);
                                } else if (type.toString().equals("class java.lang.Long")) {
                                    object = Long.parseLong(value);
                                }
                            }
                            final String filedName = names[0];
                            String _get = names[1];
                            String _upperCase = _get.toUpperCase();
                            final Operator operator = Operator.valueOf(_upperCase);
                            SearchFilter _searchFilter = new SearchFilter(filedName, operator, object);
                            filter = _searchFilter;
                        }
                        _xblockexpression = filter;
                    }
                    return _xblockexpression;
                }
            };
            Collection<SearchFilter> _map = Collections2.<Entry<String, String[]>, SearchFilter>transform(_entrySet, _function);
            Predicate<SearchFilter> nullFilter = new Predicate<SearchFilter>() {
                @Override
                public boolean apply(SearchFilter input) {
                    return input != null;
                }
            };
            Collection<SearchFilter> _filterNull = Collections2.<SearchFilter>filter(_map, nullFilter);
            _xblockexpression = Lists.newArrayList(_filterNull);
        }
        return _xblockexpression;
    }


    public static PageRequest sort(final Map<String, String[]> requestParameterMap, Integer iDisplayStart,
                                   Integer numPerPage) {
        PageRequest pageRequest = null;
        if (requestParameterMap.containsKey("order[0][dir]") && requestParameterMap.containsKey("start")
                && Integer.parseInt(requestParameterMap.get("start")[0]) < 100000) {
            String[] sorts = requestParameterMap.get("order[0][dir]");
            String[] columns = requestParameterMap.get("order[0][column]");
            String[] columNames = requestParameterMap.get("columns[" + columns[0] + "][data]");
            if (BoonPlus.isEmpty(columNames)) {
                columNames = requestParameterMap.get("columns[" + columns[0] + "][data][sort]");
            }
            pageRequest = new PageRequest(iDisplayStart / numPerPage, numPerPage,
                    sorts[0].equals("asc") ? Direction.ASC : Direction.DESC, columNames[0]);
        } else {
            pageRequest = new PageRequest(iDisplayStart / numPerPage, numPerPage);
        }
        return pageRequest;
    }
}
