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
import uk.ac.roe.wfau.firethorn.identity.Identity;

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
    Map<Action, List<Identity>> map ;

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
            map = new HashMap<Action, List<Identity>>();
            for (AccessMapField field : fields)
                {
                add(
                    field.action(),
                    field.identity()
                    );
                }
            }
        }

    private void add(Action action, Identity identity)
        {
        List<Identity> list = map.get(action);
        if (list == null)
            {
            list = new ArrayList<Identity>();
            map.put(
                action,
                list
                );
            }
        list.add(
            identity
            );
        }
    
    @Override
    public void insert(Action action, Identity identity)
        {
        this.load();
        this.add(
            action,
            identity
            );
        fields.add(
            new AccessMapField(
                entity,
                action,
                identity
                )
            );
        }

    @Override
    public void remove(Action action, Identity identity)
        {
        this.load();
        List<Identity> list = map.get(
            action
            );
        if (list != null)
            {
            list.remove(
                action
                );
            }
        }

    @Override
    public boolean contains(Action action, Identity identity)
        {
        this.load();
        return false;
        }
    }
