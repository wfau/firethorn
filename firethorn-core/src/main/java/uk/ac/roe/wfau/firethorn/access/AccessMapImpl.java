/*
 *  Copyright (C) 2013 Royal Observatory, University of Edinburgh, UK
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.roe.wfau.firethorn.access;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.Transient;

import org.hibernate.annotations.Parent;

import uk.ac.roe.wfau.firethorn.entity.Entity;
import uk.ac.roe.wfau.firethorn.identity.Authentication;

/**
 *
 *
 */
@Embeddable
@Access(
    AccessType.FIELD
    )
public class AccessMapImpl
implements AccessMap
    {

    @Parent
    private Entity entity;
    public AccessMapImpl(Entity entity)
        {
        this.entity = entity ;
        }

    @Transient        
    Map<Action, List<Authentication>> map ;

    //
    // Might be more optimal to replace this with an Iterable. 
    @ElementCollection
    @CollectionTable(
        name="AccessMapFields",
        joinColumns=
            @JoinColumn(name="entity")
        )
    private List<AccessMapField> fields = new ArrayList<AccessMapField>();
    
    private synchronized void load()
        {
        if (map == null)
            {
            map = new HashMap<Action, List<Authentication>>();
            for (AccessMapField field : fields)
                {
                add(
                    field.action(),
                    field.authentication()
                    );
                }
            }
        }

    private void add(final Action action, final Authentication authentication)
        {
        List<Authentication> list = map.get(action);
        if (list == null)
            {
            list = new ArrayList<Authentication>();
            map.put(
                action,
                list
                );
            }
        list.add(
            authentication
            );
        }
    
    @Override
    public void insert(final Action action, final Authentication authentication)
        {
        this.load();
        this.add(
            action,
            authentication
            );
        fields.add(
            new AccessMapField(
                entity,
                action,
                authentication.identity(),
                authentication.method()
                )
            );
        }

    @Override
    public void remove(final Action action, final Authentication authentication)
        {
        this.load();
        List<Authentication> list = map.get(
            action
            );
        if (list != null)
            {
            list.remove(
                authentication
                );
            }
        }

    @Override
    public boolean contains(final Action action, final Authentication authentication)
        {
        this.load();
        return false;
        }
    }
