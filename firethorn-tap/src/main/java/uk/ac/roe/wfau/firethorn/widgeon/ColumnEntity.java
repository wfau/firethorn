/*
 *
 */
package uk.ac.roe.wfau.firethorn.widgeon ;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URL;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.NamedQueries;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;  

import uk.ac.roe.wfau.firethorn.common.ident.Identifier;

import uk.ac.roe.wfau.firethorn.common.entity.AbstractEntity;
import uk.ac.roe.wfau.firethorn.common.entity.AbstractFactory;

import uk.ac.roe.wfau.firethorn.common.entity.annotation.CreateEntityMethod;
import uk.ac.roe.wfau.firethorn.common.entity.annotation.SelectEntityMethod;

/**
 * Widgeon Column implementation.
 *
 */
@Slf4j
@Entity()
@Access(
    AccessType.FIELD
    )
@Table(
    name = ColumnEntity.DB_TABLE_NAME,
    uniqueConstraints=
        @UniqueConstraint(
            columnNames = {
                AbstractEntity.DB_NAME_COL,
                ColumnEntity.DB_PARENT_COL,
                }
            )
    )
@NamedQueries(
        {
        @NamedQuery(
            name  = "widgeon.column-select-all",
            query = "FROM ColumnEntity ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "widgeon.column-select-parent",
            query = "FROM ColumnEntity WHERE parent = :parent ORDER BY ident desc"
            ),
        @NamedQuery(
            name  = "widgeon.column-select-parent.name",
            query = "FROM ColumnEntity WHERE parent = :parent AND name = :name ORDER BY ident desc"
            )
        }
    )
public class ColumnEntity
extends AbstractEntity
implements Widgeon.Schema.Catalog.Table.Column
    {

    /**
     * Our database table name.
     * 
     */
    public static final String DB_TABLE_NAME = "widgeon_column" ;

    /**
     * Our parent column name.
     * 
     */
    public static final String DB_PARENT_COL = "parent" ;

    /**
     * Schema factory interface.
     *
     */
    @Repository
    public static class Factory
    extends AbstractFactory<Widgeon.Schema.Catalog.Table.Column>
    implements Widgeon.Schema.Catalog.Table.Column.Factory
        {

        @Override
        public Class etype()
            {
            return ColumnEntity.class ;
            }

        @Override
        @SelectEntityMethod
        public Iterable<Widgeon.Schema.Catalog.Table.Column> select()
            {
            return super.iterable(
                super.query(
                    "widgeon.column-select-all"
                    )
                );
            }

        @Override
        @CreateEntityMethod
        public Widgeon.Schema.Catalog.Table.Column create(final Widgeon.Schema.Catalog.Table parent, final String name)
            {
            return super.insert(
                new ColumnEntity(
                    parent,
                    name
                    )
                );
            }

        @Override
        @SelectEntityMethod
        public Iterable<Widgeon.Schema.Catalog.Table.Column> select(final Widgeon.Schema.Catalog.Table parent)
            {
            return super.iterable(
                this.query(
                    "widgeon.column-select-parent"
                    ).setEntity(
                        "parent",
                        parent
                        )
                );
            }

        @Override
        @SelectEntityMethod
        public Widgeon.Schema.Catalog.Table.Column select(final Widgeon.Schema.Catalog.Table parent, final String name)
            {
            return super.single(
                super.query(
                    "widgeon.column-select-parent.name"
                    ).setEntity(
                        "parent",
                        parent
                    ).setString(
                        "name",
                        name
                    )
                );
            }
        }

    /**
     * Default constructor needs to be protected not private.
     * http://kristian-domagala.blogspot.co.uk/2008/10/proxy-instantiation-problem-from.html
     *
     */
    protected ColumnEntity()
        {
        super();
        }

    /**
     * Create a new Column.
     *
     */
    protected ColumnEntity(final Widgeon.Schema.Catalog.Table parent, final String name)
        {
        super(name);
        this.parent = parent ;
        }

    /**
     * Our parent Table.
     *
     */
    @ManyToOne(
        fetch = FetchType.LAZY,
        targetEntity = TableEntity.class
        )
    @JoinColumn(
        name = DB_PARENT_COL,
        unique = false,
        nullable = false,
        updatable = false
        )
    private Widgeon.Schema.Catalog.Table parent ;

    /**
     * Access to our parent Table.
     *
     */
    @Override
    public Widgeon.Schema.Catalog.Table parent()
        {
        return this.parent ;
        }

    }

