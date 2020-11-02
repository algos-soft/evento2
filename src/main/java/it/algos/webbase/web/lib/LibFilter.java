package it.algos.webbase.web.lib;

import com.google.common.collect.Iterables;
import com.vaadin.data.Container;
import com.vaadin.data.util.filter.*;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

/**
 * Created by alex on 30/12/15.
 * Source code extracted from LazyQueryContainer EntityQuery class
 */
public class LibFilter {


    /**
     * Implements conversion of a single Vaadin filter to JPA 2.0 Criteria API based predicate.
     * Supports the following operations:
     *
     * And, Between, Compare, Compare.Equal, Compare.Greater, Compare.GreaterOrEqual,
     * Compare.Less, Compare.LessOrEqual, IsNull, Like, Not, Or, SimpleStringFilter
     *
     * @param filter the Vaadin filter
     * @param cb the CriteriaBuilder
     * @param cq the CriteriaQuery
     * @param root the root
     * @return the predicate
     */
    public static Predicate getPredicate(final Container.Filter filter, final CriteriaBuilder cb,
                                         final CriteriaQuery<?> cq, final Root<?> root) {
        ArrayList<Container.Filter> filters = new ArrayList();
        filters.add(filter);
        return getPredicate(filters, cb, cq, root);
    }


    /**
     * Implements conversion of a collection of Vaadin filters to JPA 2.0 Criteria API based predicate.
     * Supports the following operations:
     *
     * And, Between, Compare, Compare.Equal, Compare.Greater, Compare.GreaterOrEqual,
     * Compare.Less, Compare.LessOrEqual, IsNull, Like, Not, Or, SimpleStringFilter
     *
     * @param cFilters a collection of Vaadin filters
     * @param cb the CriteriaBuilder
     * @param cq the CriteriaQuery
     * @param root the root
     * @return the predicate
     */
    public static Predicate getPredicate(final Collection<Container.Filter> cFilters, final CriteriaBuilder cb,
                                         final CriteriaQuery<?> cq, final Root<?> root) {



        // create a single Filter
        Container.Filter filter;
        if (cFilters.size() == 1) {
            filter = Iterables.get(cFilters, 0);
        } else {
            Container.Filter[] aFilters = cFilters.toArray(new Container.Filter[cFilters.size()]);
            filter = new And(aFilters);
        }


        if (filter instanceof And) {
            final And and = (And) filter;
            final List<Container.Filter> filters = new ArrayList<Container.Filter>(and.getFilters());

            Predicate predicate = cb.and(getPredicate(filters.remove(0), cb, cq, root),
                    getPredicate(filters.remove(0), cb, cq, root));

            while (filters.size() > 0) {
                predicate = cb.and(predicate, getPredicate(filters.remove(0), cb, cq, root));
            }

            return predicate;
        }

        if (filter instanceof Or) {
            final Or or = (Or) filter;
            final List<Container.Filter> filters = new ArrayList<Container.Filter>(or.getFilters());

            Predicate predicate = cb.or(getPredicate(filters.remove(0), cb, cq, root),
                    getPredicate(filters.remove(0), cb, cq, root));

            while (filters.size() > 0) {
                predicate = cb.or(predicate, getPredicate(filters.remove(0), cb, cq, root));
            }

            return predicate;
        }

        if (filter instanceof Not) {
            final Not not = (Not) filter;
            return cb.not(getPredicate(not.getFilter(), cb, cq, root));
        }

        if (filter instanceof Between) {
            final Between between = (Between) filter;
            final Expression property = (Expression) getPropertyPath(root, between.getPropertyId());
            return cb.between(property, (Comparable) between.getStartValue(), (Comparable) between.getEndValue());
        }

        if (filter instanceof Compare) {
            final Compare compare = (Compare) filter;
            final Expression<Comparable> property = (Expression) getPropertyPath(root, compare.getPropertyId());
            switch (compare.getOperation()) {
                case EQUAL:
                    return cb.equal(property, compare.getValue());
                case GREATER:
                    return cb.greaterThan(property, (Comparable) compare.getValue());
                case GREATER_OR_EQUAL:
                    return cb.greaterThanOrEqualTo(property, (Comparable) compare.getValue());
                case LESS:
                    return cb.lessThan(property, (Comparable) compare.getValue());
                case LESS_OR_EQUAL:
                    return cb.lessThanOrEqualTo(property, (Comparable) compare.getValue());
                default:
            }
        }

        if (filter instanceof IsNull) {
            final IsNull isNull = (IsNull) filter;
            return cb.isNull((Expression) getPropertyPath(root, isNull.getPropertyId()));
        }

        if (filter instanceof Like) {
            final Like like = (Like) filter;
            if (like.isCaseSensitive()) {
                return cb.like((Expression) getPropertyPath(root, like.getPropertyId()), like.getValue());
            } else {
                return cb.like(cb.lower((Expression) getPropertyPath(root, like.getPropertyId())),
                        like.getValue().toLowerCase());
            }
        }

        if (filter instanceof SimpleStringFilter) {
            final SimpleStringFilter simpleStringFilter = (SimpleStringFilter) filter;
            final Expression<String> property = (Expression) getPropertyPath(
                    root, simpleStringFilter.getPropertyId());
            return cb.like(property, "%"
                    + simpleStringFilter.getFilterString() + "%");
        }

        throw new UnsupportedOperationException("Vaadin filter: " + filter.getClass().getName() + " is not supported.");
    }

    /**
     * Gets property path.
     * @param root the root where path starts form
     * @param propertyId the property ID
     * @return the path to property
     */
    public static Path<Object> getPropertyPath(final Root<?> root, final Object propertyId) {
        final String[] propertyIdParts = ((String) propertyId).split("\\.");

        Path<Object> path = null;
        for (final String part : propertyIdParts) {
            if (path == null) {
                path = root.get(part);
            } else {
                path = path.get(part);
            }
        }
        return path;
    }



}
