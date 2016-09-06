/*
 *  Copyright (C) 2012 Royal Observatory, University of Edinburgh, UK
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
package uk.ac.roe.wfau.firethorn.adql.query ;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.ac.roe.wfau.firethorn.adql.query.green.GreenQuery;


/**
 *
 *
 */
public class SelectTopTestCase
extends TwomassQueryTestBase
    {

	@Test
    public void test000()
    throws Exception
        {
        final GreenQuery query = this.schema.greens().create(
            factories().adql().greens().params().create(),
            "SELECT"
            + "    TOP 5"
            + "    ra,"
            + "    dec"
            + " FROM"
            + "    adql_twomass.twomass_psc as twomass"
            + " WHERE"
            + "    ra  BETWEEN '56.0' AND '57.9'"
            + " AND"
            + "    dec BETWEEN '24.0' AND '24.2'"
            );
        debug(query);
        assertEquals(
            "SELECT TOP 5 twomass.ra AS ra , twomass.dec AS dec FROM TWOMASS.dbo.twomass_psc AS twomass WHERE twomass.ra BETWEEN '56.0' AND '57.9' AND twomass.dec BETWEEN '24.0' AND '24.2'",
            query.osql().replace('\n', ' ')
            );
        }

    @Test
    public void test001()
    throws Exception
        {
        final GreenQuery query = this.schema.greens().create(
            factories().adql().greens().params().create(),
            "SELECT"
            + "    TOP 5"
            + "    *"
            + " FROM"
            + "    adql_twomass.twomass_psc as twomass"
            + " WHERE"
            + "    ra  BETWEEN '56.0' AND '57.9'"
            + " AND"
            + "    dec BETWEEN '24.0' AND '24.2'"
            );
        debug(query);
        assertEquals(
            "SELECT TOP 5 twomass.cx AS cx,twomass.cy AS cy,twomass.cz AS cz,twomass.htmID AS htmID,twomass.ra AS ra,twomass.dec AS dec,twomass.err_maj AS err_maj,twomass.err_min AS err_min,twomass.err_ang AS err_ang,twomass.designation AS designation,twomass.j_m AS j_m,twomass.j_cmsig AS j_cmsig,twomass.j_msigcom AS j_msigcom,twomass.j_snr AS j_snr,twomass.h_m AS h_m,twomass.h_cmsig AS h_cmsig,twomass.h_msigcom AS h_msigcom,twomass.h_snr AS h_snr,twomass.k_m AS k_m,twomass.k_cmsig AS k_cmsig,twomass.k_msigcom AS k_msigcom,twomass.k_snr AS k_snr,twomass.ph_qual AS ph_qual,twomass.rd_flg AS rd_flg,twomass.bl_flg AS bl_flg,twomass.cc_flg AS cc_flg,twomass.ndet AS ndet,twomass.prox AS prox,twomass.pxpa AS pxpa,twomass.pxcntr AS pxcntr,twomass.gal_contam AS gal_contam,twomass.mp_flg AS mp_flg,twomass.pts_key AS pts_key,twomass.hemis AS hemis,twomass.date AS date,twomass.scan AS scan,twomass.glon AS glon,twomass.glat AS glat,twomass.x_scan AS x_scan,twomass.jdate AS jdate,twomass.j_psfchi AS j_psfchi,twomass.h_psfchi AS h_psfchi,twomass.k_psfchi AS k_psfchi,twomass.j_m_stdap AS j_m_stdap,twomass.j_msig_stdap AS j_msig_stdap,twomass.h_m_stdap AS h_m_stdap,twomass.h_msig_stdap AS h_msig_stdap,twomass.k_m_stdap AS k_m_stdap,twomass.k_msig_stdap AS k_msig_stdap,twomass.dist_edge_ns AS dist_edge_ns,twomass.dist_edge_ew AS dist_edge_ew,twomass.dist_edge_flg AS dist_edge_flg,twomass.dup_src AS dup_src,twomass.use_src AS use_src,twomass.a AS a,twomass.dist_opt AS dist_opt,twomass.phi_opt AS phi_opt,twomass.b_m_opt AS b_m_opt,twomass.vr_m_opt AS vr_m_opt,twomass.nopt_mchs AS nopt_mchs,twomass.ext_key AS ext_key,twomass.scan_key AS scan_key,twomass.coadd_key AS coadd_key,twomass.coadd AS coadd FROM TWOMASS.dbo.twomass_psc AS twomass WHERE twomass.ra BETWEEN '56.0' AND '57.9' AND twomass.dec BETWEEN '24.0' AND '24.2'",
            query.osql().replace('\n', ' ')
            );
        }

    @Test
    public void test002()
    throws Exception
        {
        final GreenQuery query = this.schema.greens().create(
            factories().adql().greens().params().create(),
            "SELECT"
            + "    TOP 5"
            + "    twomass.*"
            + " FROM"
            + "    adql_twomass.twomass_psc as twomass"
            + " WHERE"
            + "    ra  BETWEEN '56.0' AND '57.9'"
            + " AND"
            + "    dec BETWEEN '24.0' AND '24.2'"
            );
        debug(query);
        assertEquals(
            "SELECT TOP 5 twomass.cx AS cx,twomass.cy AS cy,twomass.cz AS cz,twomass.htmID AS htmID,twomass.ra AS ra,twomass.dec AS dec,twomass.err_maj AS err_maj,twomass.err_min AS err_min,twomass.err_ang AS err_ang,twomass.designation AS designation,twomass.j_m AS j_m,twomass.j_cmsig AS j_cmsig,twomass.j_msigcom AS j_msigcom,twomass.j_snr AS j_snr,twomass.h_m AS h_m,twomass.h_cmsig AS h_cmsig,twomass.h_msigcom AS h_msigcom,twomass.h_snr AS h_snr,twomass.k_m AS k_m,twomass.k_cmsig AS k_cmsig,twomass.k_msigcom AS k_msigcom,twomass.k_snr AS k_snr,twomass.ph_qual AS ph_qual,twomass.rd_flg AS rd_flg,twomass.bl_flg AS bl_flg,twomass.cc_flg AS cc_flg,twomass.ndet AS ndet,twomass.prox AS prox,twomass.pxpa AS pxpa,twomass.pxcntr AS pxcntr,twomass.gal_contam AS gal_contam,twomass.mp_flg AS mp_flg,twomass.pts_key AS pts_key,twomass.hemis AS hemis,twomass.date AS date,twomass.scan AS scan,twomass.glon AS glon,twomass.glat AS glat,twomass.x_scan AS x_scan,twomass.jdate AS jdate,twomass.j_psfchi AS j_psfchi,twomass.h_psfchi AS h_psfchi,twomass.k_psfchi AS k_psfchi,twomass.j_m_stdap AS j_m_stdap,twomass.j_msig_stdap AS j_msig_stdap,twomass.h_m_stdap AS h_m_stdap,twomass.h_msig_stdap AS h_msig_stdap,twomass.k_m_stdap AS k_m_stdap,twomass.k_msig_stdap AS k_msig_stdap,twomass.dist_edge_ns AS dist_edge_ns,twomass.dist_edge_ew AS dist_edge_ew,twomass.dist_edge_flg AS dist_edge_flg,twomass.dup_src AS dup_src,twomass.use_src AS use_src,twomass.a AS a,twomass.dist_opt AS dist_opt,twomass.phi_opt AS phi_opt,twomass.b_m_opt AS b_m_opt,twomass.vr_m_opt AS vr_m_opt,twomass.nopt_mchs AS nopt_mchs,twomass.ext_key AS ext_key,twomass.scan_key AS scan_key,twomass.coadd_key AS coadd_key,twomass.coadd AS coadd FROM TWOMASS.dbo.twomass_psc AS twomass WHERE twomass.ra BETWEEN '56.0' AND '57.9' AND twomass.dec BETWEEN '24.0' AND '24.2'",
            query.osql().replace('\n', ' ')
            );
        }
    }

