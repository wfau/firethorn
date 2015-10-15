/*
 *  Copyright (C) 2015 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.test;

import java.nio.ByteBuffer;
import java.util.Random;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.BaseEncoding;

/**
 *
 *
 */
@Slf4j
public class NameTestBase
    {

    private Long uidlo ;
    private Long uidhi ;

    protected static final Random random = new Random(
        System.currentTimeMillis()
        );

    @Test
    public void test001()
    throws Exception
        {
        for (int i = 0 ; i < 100 ; i++)
            {
            this.uidlo = System.currentTimeMillis();
            this.uidhi = random.nextLong();
    
            final BaseEncoding encoder = BaseEncoding.base32().omitPadding() ;

            final byte[] lobytes = new byte[8];
            final ByteBuffer lobuffer = ByteBuffer.wrap(lobytes).putLong(uidlo);
            final String lostring = encoder.encode(lobytes);
    
            final byte[] hibytes = new byte[8];
            final ByteBuffer hibuffer = ByteBuffer.wrap(hibytes).putLong(uidhi);
            final String histring = encoder.encode(hibytes);
    
            log.debug("[{}_{}]", lostring, histring);
    
            }
        }
    }
