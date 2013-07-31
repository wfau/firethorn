#!/usr/bin/env python
""" generated source for module SLALIB """
#  Static SLALIB like routines using Eclipse
# package: net.mar
import math

class SLALIB(object):
    """ generated source for class SLALIB """
    d2s = 86400.0

    @classmethod
    def DSEP(cls, A1, B1, A2, B2):
        """ generated source for method DSEP """
        # 
        #          * *+
        # *     - - - - -
        # *      D S E P
        # *     - - - - -
        # *
        # *  Angle between two points on a sphere.
        # *
        # *  (double precision)
        # *
        # *  Given:
        # *     A1,B1    d     spherical coordinates of one point
        # *     A2,B2    d     spherical coordinates of the other point
        # *
        # *  (The spherical coordinates are [RA,Dec], [Long,Lat] etc, in radians.)
        # *
        # *  The result is the angle, in radians, between the two points.  It
        # *  is always positive.
        # *
        # *  Called:  sla_DCS2C, sla_DSEPV
        # *
        # *  Last revision:   7 May 2000
        # *
        # *  Copyright P.T.Wallace.  All rights reserved.
        # *
        # *-
        #          
        V1 = [None]*3
        V2 = [None]*3
        #   Convert coordinates from spherical to Cartesian.
        V1 = SLALIB.DCS2C(A1, B1)
        V2 = SLALIB.DCS2C(A2, B2)
        return SLALIB.DSEPV(V1, V2)

    @classmethod
    def DSEPV(cls, V1, V2):
        """ generated source for method DSEPV """
        # 
        #          * *+
        # *     - - - - - -
        # *      D S E P V
        # *     - - - - - -
        # *
        # *  Angle between two vectors.
        # *
        # *  (double precision)
        # *
        # *  Given:
        # *     V1      d(3)    first vector
        # *     V2      d(3)    second vector
        # *
        # *  The result is the angle, in radians, between the two vectors.  It
        # *  is always positive.
        # *
        # *  Notes:
        # *
        # *  1  There is no requirement for the vectors to be unit length.
        # *
        # *  2  If either vector is null, zero is returned.
        # *
        # *  3  The simplest formulation would use dot product alone.  However,
        # *     this would reduce the accuracy for angles near zero and pi.  The
        # *     algorithm uses both cross product and dot product, which maintains
        # *     accuracy for all sizes of angle.
        # *
        # *  Called:  sla_DVXV, sla_DVN, sla_DVDV
        #          
        V1XV2 = [None]*3
        WV = [None]*4
        S = float()
        C = float()
        # sla_DVDV;
        sla_DSEPV = float()
        V1XV2 = SLALIB.DVXV(V1, V2)
        WV = SLALIB.DVN(V1XV2)
        #  Wv[3] is S
        S = WV[3]
        # *  Dot product = cosine multiplied by the two moduli.
        C = SLALIB.DVDV(V1, V2)
        # *  Angle between the vectors.
        if S != 0.0:
            sla_DSEPV = math.atan2(S, C)
        else:
            sla_DSEPV = 0.0
        return sla_DSEPV

    @classmethod
    def DVDV(cls, VA, VB):
        """ generated source for method DVDV """
        # 
        #          * *+
        # *     - - - - -
        # *      D V D V
        # *     - - - - -
        # *
        # *  Scalar product of two 3-vectors  (double precision)
        # *
        # *  Given:
        # *      VA      dp(3)     first vector
        # *      VB      dp(3)     second vector
        # *
        # *  The result is the scalar product VA.VB (double precision)
        # *
        # *  P.T.Wallace   Starlink   November 1984
        # *
        # *  Copyright (C) 1995 Rutherford Appleton Laboratory
        # *
        #          
        return VA[0] * VB[0] + VA[1] * VB[1] + VA[2] * VB[2]

    @classmethod
    def DVN(cls, V):
        """ generated source for method DVN """
        # 
        #          * *+
        # *     - - - -
        # *      D V N
        # *     - - - -
        # *
        # *  Normalizes a 3-vector also giving the modulus (double precision)
        # *
        # *  Given:
        # *     V       dp(3)      vector
        # *
        # *  Returned:
        # *     UV      dp(3)      unit vector in direction of V
        # *     VM      dp         modulus of V
        # *
        # *  If the modulus of V is zero, UV is set to zero as well
        # *
        # *  P.T.Wallace   Starlink   23 November 1995
        #          
        UV = [None]*4
        #  4 not 3 
        VM = float()
        I = int()
        W1 = float()
        W2 = float()
        # *  Modulus
        W1 = 0.0
        #  Static SLALIB like routines using Eclipse
        # 
        #          * *+
        # *     - - - - -
        # *      D S E P
        # *     - - - - -
        # *
        # *  Angle between two points on a sphere.
        # *
        # *  (double precision)
        # *
        # *  Given:
        # *     A1,B1    d     spherical coordinates of one point
        # *     A2,B2    d     spherical coordinates of the other point
        # *
        # *  (The spherical coordinates are [RA,Dec], [Long,Lat] etc, in radians.)
        # *
        # *  The result is the angle, in radians, between the two points.  It
        # *  is always positive.
        # *
        # *  Called:  sla_DCS2C, sla_DSEPV
        # *
        # *  Last revision:   7 May 2000
        # *
        # *  Copyright P.T.Wallace.  All rights reserved.
        # *
        # *-
        #          
        #   Convert coordinates from spherical to Cartesian.
        # 
        #          * *+
        # *     - - - - - -
        # *      D S E P V
        # *     - - - - - -
        # *
        # *  Angle between two vectors.
        # *
        # *  (double precision)
        # *
        # *  Given:
        # *     V1      d(3)    first vector
        # *     V2      d(3)    second vector
        # *
        # *  The result is the angle, in radians, between the two vectors.  It
        # *  is always positive.
        # *
        # *  Notes:
        # *
        # *  1  There is no requirement for the vectors to be unit length.
        # *
        # *  2  If either vector is null, zero is returned.
        # *
        # *  3  The simplest formulation would use dot product alone.  However,
        # *     this would reduce the accuracy for angles near zero and pi.  The
        # *     algorithm uses both cross product and dot product, which maintains
        # *     accuracy for all sizes of angle.
        # *
        # *  Called:  sla_DVXV, sla_DVN, sla_DVDV
        #          
        # sla_DVDV;
        #  Wv[3] is S
        # *  Dot product = cosine multiplied by the two moduli.
        # *  Angle between the vectors.
        # 
        #          * *+
        # *     - - - - -
        # *      D V D V
        # *     - - - - -
        # *
        # *  Scalar product of two 3-vectors  (double precision)
        # *
        # *  Given:
        # *      VA      dp(3)     first vector
        # *      VB      dp(3)     second vector
        # *
        # *  The result is the scalar product VA.VB (double precision)
        # *
        # *  P.T.Wallace   Starlink   November 1984
        # *
        # *  Copyright (C) 1995 Rutherford Appleton Laboratory
        # *
        #          
        # 
        #          * *+
        # *     - - - -
        # *      D V N
        # *     - - - -
        # *
        # *  Normalizes a 3-vector also giving the modulus (double precision)
        # *
        # *  Given:
        # *     V       dp(3)      vector
        # *
        # *  Returned:
        # *     UV      dp(3)      unit vector in direction of V
        # *     VM      dp         modulus of V
        # *
        # *  If the modulus of V is zero, UV is set to zero as well
        # *
        # *  P.T.Wallace   Starlink   23 November 1995
        #          
        #  4 not 3 
        # *  Modulus
        while I < 3:
            #  Static SLALIB like routines using Eclipse
            # 
            #          * *+
            # *     - - - - -
            # *      D S E P
            # *     - - - - -
            # *
            # *  Angle between two points on a sphere.
            # *
            # *  (double precision)
            # *
            # *  Given:
            # *     A1,B1    d     spherical coordinates of one point
            # *     A2,B2    d     spherical coordinates of the other point
            # *
            # *  (The spherical coordinates are [RA,Dec], [Long,Lat] etc, in radians.)
            # *
            # *  The result is the angle, in radians, between the two points.  It
            # *  is always positive.
            # *
            # *  Called:  sla_DCS2C, sla_DSEPV
            # *
            # *  Last revision:   7 May 2000
            # *
            # *  Copyright P.T.Wallace.  All rights reserved.
            # *
            # *-
            #          
            #   Convert coordinates from spherical to Cartesian.
            # 
            #          * *+
            # *     - - - - - -
            # *      D S E P V
            # *     - - - - - -
            # *
            # *  Angle between two vectors.
            # *
            # *  (double precision)
            # *
            # *  Given:
            # *     V1      d(3)    first vector
            # *     V2      d(3)    second vector
            # *
            # *  The result is the angle, in radians, between the two vectors.  It
            # *  is always positive.
            # *
            # *  Notes:
            # *
            # *  1  There is no requirement for the vectors to be unit length.
            # *
            # *  2  If either vector is null, zero is returned.
            # *
            # *  3  The simplest formulation would use dot product alone.  However,
            # *     this would reduce the accuracy for angles near zero and pi.  The
            # *     algorithm uses both cross product and dot product, which maintains
            # *     accuracy for all sizes of angle.
            # *
            # *  Called:  sla_DVXV, sla_DVN, sla_DVDV
            #          
            # sla_DVDV;
            #  Wv[3] is S
            # *  Dot product = cosine multiplied by the two moduli.
            # *  Angle between the vectors.
            # 
            #          * *+
            # *     - - - - -
            # *      D V D V
            # *     - - - - -
            # *
            # *  Scalar product of two 3-vectors  (double precision)
            # *
            # *  Given:
            # *      VA      dp(3)     first vector
            # *      VB      dp(3)     second vector
            # *
            # *  The result is the scalar product VA.VB (double precision)
            # *
            # *  P.T.Wallace   Starlink   November 1984
            # *
            # *  Copyright (C) 1995 Rutherford Appleton Laboratory
            # *
            #          
            # 
            #          * *+
            # *     - - - -
            # *      D V N
            # *     - - - -
            # *
            # *  Normalizes a 3-vector also giving the modulus (double precision)
            # *
            # *  Given:
            # *     V       dp(3)      vector
            # *
            # *  Returned:
            # *     UV      dp(3)      unit vector in direction of V
            # *     VM      dp         modulus of V
            # *
            # *  If the modulus of V is zero, UV is set to zero as well
            # *
            # *  P.T.Wallace   Starlink   23 November 1995
            #          
            #  4 not 3 
            # *  Modulus
            W2 = V[I]
            W1 = W1 + W2 * W2
            I += 1
        W1 = math.sqrt(W1)
        VM = W1
        #  *  Normalize the vector
        if W1 <= 0.0:
            W1 = 1.0
        #  Static SLALIB like routines using Eclipse
        # 
        #          * *+
        # *     - - - - -
        # *      D S E P
        # *     - - - - -
        # *
        # *  Angle between two points on a sphere.
        # *
        # *  (double precision)
        # *
        # *  Given:
        # *     A1,B1    d     spherical coordinates of one point
        # *     A2,B2    d     spherical coordinates of the other point
        # *
        # *  (The spherical coordinates are [RA,Dec], [Long,Lat] etc, in radians.)
        # *
        # *  The result is the angle, in radians, between the two points.  It
        # *  is always positive.
        # *
        # *  Called:  sla_DCS2C, sla_DSEPV
        # *
        # *  Last revision:   7 May 2000
        # *
        # *  Copyright P.T.Wallace.  All rights reserved.
        # *
        # *-
        #          
        #   Convert coordinates from spherical to Cartesian.
        # 
        #          * *+
        # *     - - - - - -
        # *      D S E P V
        # *     - - - - - -
        # *
        # *  Angle between two vectors.
        # *
        # *  (double precision)
        # *
        # *  Given:
        # *     V1      d(3)    first vector
        # *     V2      d(3)    second vector
        # *
        # *  The result is the angle, in radians, between the two vectors.  It
        # *  is always positive.
        # *
        # *  Notes:
        # *
        # *  1  There is no requirement for the vectors to be unit length.
        # *
        # *  2  If either vector is null, zero is returned.
        # *
        # *  3  The simplest formulation would use dot product alone.  However,
        # *     this would reduce the accuracy for angles near zero and pi.  The
        # *     algorithm uses both cross product and dot product, which maintains
        # *     accuracy for all sizes of angle.
        # *
        # *  Called:  sla_DVXV, sla_DVN, sla_DVDV
        #          
        # sla_DVDV;
        #  Wv[3] is S
        # *  Dot product = cosine multiplied by the two moduli.
        # *  Angle between the vectors.
        # 
        #          * *+
        # *     - - - - -
        # *      D V D V
        # *     - - - - -
        # *
        # *  Scalar product of two 3-vectors  (double precision)
        # *
        # *  Given:
        # *      VA      dp(3)     first vector
        # *      VB      dp(3)     second vector
        # *
        # *  The result is the scalar product VA.VB (double precision)
        # *
        # *  P.T.Wallace   Starlink   November 1984
        # *
        # *  Copyright (C) 1995 Rutherford Appleton Laboratory
        # *
        #          
        # 
        #          * *+
        # *     - - - -
        # *      D V N
        # *     - - - -
        # *
        # *  Normalizes a 3-vector also giving the modulus (double precision)
        # *
        # *  Given:
        # *     V       dp(3)      vector
        # *
        # *  Returned:
        # *     UV      dp(3)      unit vector in direction of V
        # *     VM      dp         modulus of V
        # *
        # *  If the modulus of V is zero, UV is set to zero as well
        # *
        # *  P.T.Wallace   Starlink   23 November 1995
        #          
        #  4 not 3 
        # *  Modulus
        #  *  Normalize the vector
        while I < 3:
            #  Static SLALIB like routines using Eclipse
            # 
            #          * *+
            # *     - - - - -
            # *      D S E P
            # *     - - - - -
            # *
            # *  Angle between two points on a sphere.
            # *
            # *  (double precision)
            # *
            # *  Given:
            # *     A1,B1    d     spherical coordinates of one point
            # *     A2,B2    d     spherical coordinates of the other point
            # *
            # *  (The spherical coordinates are [RA,Dec], [Long,Lat] etc, in radians.)
            # *
            # *  The result is the angle, in radians, between the two points.  It
            # *  is always positive.
            # *
            # *  Called:  sla_DCS2C, sla_DSEPV
            # *
            # *  Last revision:   7 May 2000
            # *
            # *  Copyright P.T.Wallace.  All rights reserved.
            # *
            # *-
            #          
            #   Convert coordinates from spherical to Cartesian.
            # 
            #          * *+
            # *     - - - - - -
            # *      D S E P V
            # *     - - - - - -
            # *
            # *  Angle between two vectors.
            # *
            # *  (double precision)
            # *
            # *  Given:
            # *     V1      d(3)    first vector
            # *     V2      d(3)    second vector
            # *
            # *  The result is the angle, in radians, between the two vectors.  It
            # *  is always positive.
            # *
            # *  Notes:
            # *
            # *  1  There is no requirement for the vectors to be unit length.
            # *
            # *  2  If either vector is null, zero is returned.
            # *
            # *  3  The simplest formulation would use dot product alone.  However,
            # *     this would reduce the accuracy for angles near zero and pi.  The
            # *     algorithm uses both cross product and dot product, which maintains
            # *     accuracy for all sizes of angle.
            # *
            # *  Called:  sla_DVXV, sla_DVN, sla_DVDV
            #          
            # sla_DVDV;
            #  Wv[3] is S
            # *  Dot product = cosine multiplied by the two moduli.
            # *  Angle between the vectors.
            # 
            #          * *+
            # *     - - - - -
            # *      D V D V
            # *     - - - - -
            # *
            # *  Scalar product of two 3-vectors  (double precision)
            # *
            # *  Given:
            # *      VA      dp(3)     first vector
            # *      VB      dp(3)     second vector
            # *
            # *  The result is the scalar product VA.VB (double precision)
            # *
            # *  P.T.Wallace   Starlink   November 1984
            # *
            # *  Copyright (C) 1995 Rutherford Appleton Laboratory
            # *
            #          
            # 
            #          * *+
            # *     - - - -
            # *      D V N
            # *     - - - -
            # *
            # *  Normalizes a 3-vector also giving the modulus (double precision)
            # *
            # *  Given:
            # *     V       dp(3)      vector
            # *
            # *  Returned:
            # *     UV      dp(3)      unit vector in direction of V
            # *     VM      dp         modulus of V
            # *
            # *  If the modulus of V is zero, UV is set to zero as well
            # *
            # *  P.T.Wallace   Starlink   23 November 1995
            #          
            #  4 not 3 
            # *  Modulus
            #  *  Normalize the vector
            UV[I] = V[I] / W1
            I += 1
        UV[3] = VM
        return UV

    @classmethod
    def DVXV(cls, VA, VB):
        """ generated source for method DVXV """
        # 
        #          * *+
        # *     - - - - -
        # *      D V X V
        # *     - - - - -
        # *
        # *  Vector product of two 3-vectors  (double precision)
        # *
        # *  Given:
        # *      VA      dp(3)     first vector
        # *      VB      dp(3)     second vector
        # *
        # *  Returned:
        # *      VC      dp(3)     vector result
        # *
        # *  P.T.Wallace   Starlink   March 1986
        # *
        # *  Copyright (C) 1995 Rutherford Appleton Laboratory
        # *
        #          
        VC = [None]*3
        VW = [None]*3
        I = int()
        # *  Form the vector product VA cross VB
        VW[0] = VA[1] * VB[2] - VA[2] * VB[1]
        VW[1] = VA[2] * VB[0] - VA[0] * VB[2]
        VW[2] = VA[0] * VB[1] - VA[1] * VB[0]
        # *  Return the result
        #  Static SLALIB like routines using Eclipse
        # 
        #          * *+
        # *     - - - - -
        # *      D S E P
        # *     - - - - -
        # *
        # *  Angle between two points on a sphere.
        # *
        # *  (double precision)
        # *
        # *  Given:
        # *     A1,B1    d     spherical coordinates of one point
        # *     A2,B2    d     spherical coordinates of the other point
        # *
        # *  (The spherical coordinates are [RA,Dec], [Long,Lat] etc, in radians.)
        # *
        # *  The result is the angle, in radians, between the two points.  It
        # *  is always positive.
        # *
        # *  Called:  sla_DCS2C, sla_DSEPV
        # *
        # *  Last revision:   7 May 2000
        # *
        # *  Copyright P.T.Wallace.  All rights reserved.
        # *
        # *-
        #          
        #   Convert coordinates from spherical to Cartesian.
        # 
        #          * *+
        # *     - - - - - -
        # *      D S E P V
        # *     - - - - - -
        # *
        # *  Angle between two vectors.
        # *
        # *  (double precision)
        # *
        # *  Given:
        # *     V1      d(3)    first vector
        # *     V2      d(3)    second vector
        # *
        # *  The result is the angle, in radians, between the two vectors.  It
        # *  is always positive.
        # *
        # *  Notes:
        # *
        # *  1  There is no requirement for the vectors to be unit length.
        # *
        # *  2  If either vector is null, zero is returned.
        # *
        # *  3  The simplest formulation would use dot product alone.  However,
        # *     this would reduce the accuracy for angles near zero and pi.  The
        # *     algorithm uses both cross product and dot product, which maintains
        # *     accuracy for all sizes of angle.
        # *
        # *  Called:  sla_DVXV, sla_DVN, sla_DVDV
        #          
        # sla_DVDV;
        #  Wv[3] is S
        # *  Dot product = cosine multiplied by the two moduli.
        # *  Angle between the vectors.
        # 
        #          * *+
        # *     - - - - -
        # *      D V D V
        # *     - - - - -
        # *
        # *  Scalar product of two 3-vectors  (double precision)
        # *
        # *  Given:
        # *      VA      dp(3)     first vector
        # *      VB      dp(3)     second vector
        # *
        # *  The result is the scalar product VA.VB (double precision)
        # *
        # *  P.T.Wallace   Starlink   November 1984
        # *
        # *  Copyright (C) 1995 Rutherford Appleton Laboratory
        # *
        #          
        # 
        #          * *+
        # *     - - - -
        # *      D V N
        # *     - - - -
        # *
        # *  Normalizes a 3-vector also giving the modulus (double precision)
        # *
        # *  Given:
        # *     V       dp(3)      vector
        # *
        # *  Returned:
        # *     UV      dp(3)      unit vector in direction of V
        # *     VM      dp         modulus of V
        # *
        # *  If the modulus of V is zero, UV is set to zero as well
        # *
        # *  P.T.Wallace   Starlink   23 November 1995
        #          
        #  4 not 3 
        # *  Modulus
        #  *  Normalize the vector
        # 
        #          * *+
        # *     - - - - -
        # *      D V X V
        # *     - - - - -
        # *
        # *  Vector product of two 3-vectors  (double precision)
        # *
        # *  Given:
        # *      VA      dp(3)     first vector
        # *      VB      dp(3)     second vector
        # *
        # *  Returned:
        # *      VC      dp(3)     vector result
        # *
        # *  P.T.Wallace   Starlink   March 1986
        # *
        # *  Copyright (C) 1995 Rutherford Appleton Laboratory
        # *
        #          
        # *  Form the vector product VA cross VB
        # *  Return the result
        while I < 3:
            #  Static SLALIB like routines using Eclipse
            # 
            #          * *+
            # *     - - - - -
            # *      D S E P
            # *     - - - - -
            # *
            # *  Angle between two points on a sphere.
            # *
            # *  (double precision)
            # *
            # *  Given:
            # *     A1,B1    d     spherical coordinates of one point
            # *     A2,B2    d     spherical coordinates of the other point
            # *
            # *  (The spherical coordinates are [RA,Dec], [Long,Lat] etc, in radians.)
            # *
            # *  The result is the angle, in radians, between the two points.  It
            # *  is always positive.
            # *
            # *  Called:  sla_DCS2C, sla_DSEPV
            # *
            # *  Last revision:   7 May 2000
            # *
            # *  Copyright P.T.Wallace.  All rights reserved.
            # *
            # *-
            #          
            #   Convert coordinates from spherical to Cartesian.
            # 
            #          * *+
            # *     - - - - - -
            # *      D S E P V
            # *     - - - - - -
            # *
            # *  Angle between two vectors.
            # *
            # *  (double precision)
            # *
            # *  Given:
            # *     V1      d(3)    first vector
            # *     V2      d(3)    second vector
            # *
            # *  The result is the angle, in radians, between the two vectors.  It
            # *  is always positive.
            # *
            # *  Notes:
            # *
            # *  1  There is no requirement for the vectors to be unit length.
            # *
            # *  2  If either vector is null, zero is returned.
            # *
            # *  3  The simplest formulation would use dot product alone.  However,
            # *     this would reduce the accuracy for angles near zero and pi.  The
            # *     algorithm uses both cross product and dot product, which maintains
            # *     accuracy for all sizes of angle.
            # *
            # *  Called:  sla_DVXV, sla_DVN, sla_DVDV
            #          
            # sla_DVDV;
            #  Wv[3] is S
            # *  Dot product = cosine multiplied by the two moduli.
            # *  Angle between the vectors.
            # 
            #          * *+
            # *     - - - - -
            # *      D V D V
            # *     - - - - -
            # *
            # *  Scalar product of two 3-vectors  (double precision)
            # *
            # *  Given:
            # *      VA      dp(3)     first vector
            # *      VB      dp(3)     second vector
            # *
            # *  The result is the scalar product VA.VB (double precision)
            # *
            # *  P.T.Wallace   Starlink   November 1984
            # *
            # *  Copyright (C) 1995 Rutherford Appleton Laboratory
            # *
            #          
            # 
            #          * *+
            # *     - - - -
            # *      D V N
            # *     - - - -
            # *
            # *  Normalizes a 3-vector also giving the modulus (double precision)
            # *
            # *  Given:
            # *     V       dp(3)      vector
            # *
            # *  Returned:
            # *     UV      dp(3)      unit vector in direction of V
            # *     VM      dp         modulus of V
            # *
            # *  If the modulus of V is zero, UV is set to zero as well
            # *
            # *  P.T.Wallace   Starlink   23 November 1995
            #          
            #  4 not 3 
            # *  Modulus
            #  *  Normalize the vector
            # 
            #          * *+
            # *     - - - - -
            # *      D V X V
            # *     - - - - -
            # *
            # *  Vector product of two 3-vectors  (double precision)
            # *
            # *  Given:
            # *      VA      dp(3)     first vector
            # *      VB      dp(3)     second vector
            # *
            # *  Returned:
            # *      VC      dp(3)     vector result
            # *
            # *  P.T.Wallace   Starlink   March 1986
            # *
            # *  Copyright (C) 1995 Rutherford Appleton Laboratory
            # *
            #          
            # *  Form the vector product VA cross VB
            # *  Return the result
            VC[I] = VW[I]
            I += 1
        return VC

    @classmethod
    def DCS2C(cls, A, B):
        """ generated source for method DCS2C """
        # 
        #  * * Spherical coordinates to direction cosines (double precision)
        #  * 
        #  * Given: A,B dp spherical coordinates in radians (RA,Dec), (Long,Lat) etc
        #  * 
        #  * Returned: V dp(3) x,y,z unit vector
        #  * 
        #  * The spherical coordinates are longitude (+ve anticlockwise looking from the
        #  * +ve latitude pole) and latitude. The Cartesian coordinates are right handed,
        #  * with the x axis at zero longitude and latitude, and the z axis at the +ve
        #  * latitude pole.
        #  *  
        #  
        V = [None]*3
        COSB = math.cos(B)
        V[0] = math.cos(A) * COSB
        V[1] = math.sin(A) * COSB
        V[2] = math.sin(B)
        return V

    @classmethod
    def DCC2S(cls, V):
        """ generated source for method DCC2S """
        # 
        #  * * Direction cosines to spherical coordinates (double precision)
        #  * 
        #  * Given: V d(3) x,y,z vector
        #  * 
        #  * Returned: A,B d spherical coordinates in radians
        #  * 
        #  * The spherical coordinates are longitude (+ve anticlockwise looking from the
        #  * +ve latitude pole) and latitude. The Cartesian coordinates are right handed,
        #  * with the x axis at zero longitude and latitude, and the z axis at the +ve
        #  * latitude pole.
        #  * 
        #  * If V is null, zero A and B are returned. At either pole, zero A is returned.
        #  *  
        #  
        X = V[0]
        Y = V[1]
        Z = V[2]
        A = float()
        B = float()
        R = math.sqrt(X * X + Y * Y)
        if R == 0.0:
            A = 0.0
        else:
            A = math.atan2(Y, X)
        if Z == 0.0:
            B = 0.0
        else:
            B = math.atan2(Z, R)
        coords = [None]*2
        coords[0] = A
        coords[1] = B
        return coords
    
   
    
    @classmethod
    def GALEQ(cls, DL, DB):
        """ generated source for method GALEQ """
    
        RMAT = [[[],[],[]],[[],[],[]],[[],[],[]]]
        RMAT[0][0] = -0.054875539726
        RMAT[0][1] = -0.873437108010
        RMAT[0][2] = -0.483834985808
        RMAT[1][0] = +0.494109453312
        RMAT[1][1] = -0.444829589425
        RMAT[1][2] = +0.746982251810
        RMAT[2][0] = -0.867666135858
        RMAT[2][1] = -0.198076386122
        RMAT[2][2] = +0.455983795705
        V1 = [None]*3
        V2 = [None]*3
        V1 = SLALIB.DCS2C(DL, DB)
        V2 = SLALIB.DIMXV(RMAT, V1)
        temp = [None]*2
        temp = SLALIB.DCC2S(V2)
        coords = [None]*2
        coords[0] = SLALIB.DRANRM(temp[0])
        coords[1] = SLALIB.DRANGE(temp[1])
        return coords

  
    @classmethod
    def DRANRM(cls, ANGLE):
        """ generated source for method DRANRM """
        D2PI = math.pi * 2
        dranrm = ANGLE % D2PI
        if dranrm < 0:
            dranrm = dranrm + D2PI
        return dranrm
    
    @classmethod
    def DTP2S(cls, XI, ETA, RAZ, DECZ):
        """ generated source for method DTP2S """
        coords = [None]*2
        SDECZ = math.sin(DECZ)
        CDECZ = math.cos(DECZ)
        DENOM = CDECZ - ETA * SDECZ
        coords[0] = SLALIB.DRANRM(math.atan2(XI, DENOM) + RAZ)
        coords[1] = math.atan2(SDECZ + ETA * CDECZ, math.sqrt(XI * XI + DENOM * DENOM))
        return coords
    @classmethod
    
    def SIGN(cls, a1, a2):
        """ generated source for method SIGN """
        if a2 >= 0.0:
            return abs(a1)
        else:
            return -abs(a1)


    @classmethod
    def DS2TP(cls, RA, DEC, RAZ, DECZ):
        """ generated source for method DS2TP """
        results = [None]*3
        TINY = 1E-6
        SDECZ = math.sin(DECZ)
        SDEC = math.sin(DEC)
        CDECZ = math.cos(DECZ)
        CDEC = math.cos(DEC)
        RADIF = RA - RAZ
        SRADIF = math.sin(RADIF)
        CRADIF = math.cos(RADIF)
        DENOM = SDEC * SDECZ + CDEC * CDECZ * CRADIF
        if DENOM > TINY:
            results[2] = 0.0
        elif DENOM >= 0.0:
            results[2] = 1.0
            DENOM = TINY
        elif DENOM > -TINY:
            results[2] = 2.0
            DENOM = -TINY
        else:
            results[2] = 3.0
        results[0] = CDEC * SRADIF / DENOM
        results[1] = (SDEC * CDECZ - CDEC * SDECZ * CRADIF) / DENOM
        return results


    @classmethod
    def EPB2D(cls, EPB):
        """ generated source for method EPB2D """
        return 15019.81352 + (EPB - 1900.0) * 365.242198781

    @classmethod
    def EPJ(cls, DATE):
        """ generated source for method EPJ """
        return 2000.0 + (DATE - 51544.5) / 365.25


    @classmethod
    def DRANGE(cls, ANGLE):
        """ generated source for method DRANGE """
        D2PI = math.pi * 2
        DPI = math.pi
        drange = ANGLE % D2PI
        if abs(drange) >= DPI:
            if ANGLE >= 0.0:
                drange = drange - D2PI
            else:
                drange = drange + D2PI
        return drange

    @classmethod
    def DIMXV(cls, DM, VA):
        """ generated source for method DIMXV """
        W = float()
        VW = [None]*3
        VB = [None]*3
        #  Static SLALIB like routines using Eclipse
        # 
        #          * *+
        # *     - - - - -
        # *      D S E P
        # *     - - - - -
        # *
        # *  Angle between two points on a sphere.
        # *
        # *  (double precision)
        # *
        # *  Given:
        # *     A1,B1    d     spherical coordinates of one point
        # *     A2,B2    d     spherical coordinates of the other point
        # *
        # *  (The spherical coordinates are [RA,Dec], [Long,Lat] etc, in radians.)
        # *
        # *  The result is the angle, in radians, between the two points.  It
        # *  is always positive.
        # *
        # *  Called:  sla_DCS2C, sla_DSEPV
        # *
        # *  Last revision:   7 May 2000
        # *
        # *  Copyright P.T.Wallace.  All rights reserved.
        # *
        # *-
        #          
        #   Convert coordinates from spherical to Cartesian.
        # 
        #          * *+
        # *     - - - - - -
        # *      D S E P V
        # *     - - - - - -
        # *
        # *  Angle between two vectors.
        # *
        # *  (double precision)
        # *
        # *  Given:
        # *     V1      d(3)    first vector
        # *     V2      d(3)    second vector
        # *
        # *  The result is the angle, in radians, between the two vectors.  It
        # *  is always positive.
        # *
        # *  Notes:
        # *
        # *  1  There is no requirement for the vectors to be unit length.
        # *
        # *  2  If either vector is null, zero is returned.
        # *
        # *  3  The simplest formulation would use dot product alone.  However,
        # *     this would reduce the accuracy for angles near zero and pi.  The
        # *     algorithm uses both cross product and dot product, which maintains
        # *     accuracy for all sizes of angle.
        # *
        # *  Called:  sla_DVXV, sla_DVN, sla_DVDV
        #          
        # sla_DVDV;
        #  Wv[3] is S
        # *  Dot product = cosine multiplied by the two moduli.
        # *  Angle between the vectors.
        # 
        #          * *+
        # *     - - - - -
        # *      D V D V
        # *     - - - - -
        # *
        # *  Scalar product of two 3-vectors  (double precision)
        # *
        # *  Given:
        # *      VA      dp(3)     first vector
        # *      VB      dp(3)     second vector
        # *
        # *  The result is the scalar product VA.VB (double precision)
        # *
        # *  P.T.Wallace   Starlink   November 1984
        # *
        # *  Copyright (C) 1995 Rutherford Appleton Laboratory
        # *
        #          
        # 
        #          * *+
        # *     - - - -
        # *      D V N
        # *     - - - -
        # *
        # *  Normalizes a 3-vector also giving the modulus (double precision)
        # *
        # *  Given:
        # *     V       dp(3)      vector
        # *
        # *  Returned:
        # *     UV      dp(3)      unit vector in direction of V
        # *     VM      dp         modulus of V
        # *
        # *  If the modulus of V is zero, UV is set to zero as well
        # *
        # *  P.T.Wallace   Starlink   23 November 1995
        #          
        #  4 not 3 
        # *  Modulus
        #  *  Normalize the vector
        # 
        #          * *+
        # *     - - - - -
        # *      D V X V
        # *     - - - - -
        # *
        # *  Vector product of two 3-vectors  (double precision)
        # *
        # *  Given:
        # *      VA      dp(3)     first vector
        # *      VB      dp(3)     second vector
        # *
        # *  Returned:
        # *      VC      dp(3)     vector result
        # *
        # *  P.T.Wallace   Starlink   March 1986
        # *
        # *  Copyright (C) 1995 Rutherford Appleton Laboratory
        # *
        #          
        # *  Form the vector product VA cross VB
        # *  Return the result
        # 
        #  * * Spherical coordinates to direction cosines (double precision)
        #  * 
        #  * Given: A,B dp spherical coordinates in radians (RA,Dec), (Long,Lat) etc
        #  * 
        #  * Returned: V dp(3) x,y,z unit vector
        #  * 
        #  * The spherical coordinates are longitude (+ve anticlockwise looking from the
        #  * +ve latitude pole) and latitude. The Cartesian coordinates are right handed,
        #  * with the x axis at zero longitude and latitude, and the z axis at the +ve
        #  * latitude pole.
        #  *  
        #  
        # 
        #  * * Direction cosines to spherical coordinates (double precision)
        #  * 
        #  * Given: V d(3) x,y,z vector
        #  * 
        #  * Returned: A,B d spherical coordinates in radians
        #  * 
        #  * The spherical coordinates are longitude (+ve anticlockwise looking from the
        #  * +ve latitude pole) and latitude. The Cartesian coordinates are right handed,
        #  * with the x axis at zero longitude and latitude, and the z axis at the +ve
        #  * latitude pole.
        #  * 
        #  * If V is null, zero A and B are returned. At either pole, zero A is returned.
        #  *  
        #  
        # 
        #  * * Convert B1950.0 FK4 star data to J2000.0 FK5 assuming zero proper motion in
        #  * the FK5 frame (double precision)
        #  * 
        #  * This routine converts stars from the old, Bessel-Newcomb, FK4 system to the
        #  * new, IAU 1976, FK5, Fricke system, in such a way that the FK5 proper motion
        #  * is zero. Because such a star has, in general, a non-zero proper motion in the
        #  * FK4 system, the routine requires the epoch at which the position in the FK4
        #  * system was determined.
        #  * 
        #  * The method is from Appendix 2 of Ref 1, but using the constants of Ref 4.
        #  * 
        #  * Given: R1950,D1950 dp B1950.0 FK4 RA,Dec at epoch (rad) BEPOCH dp Besselian
        #  * epoch (e.g. 1979.3D0)
        #  * 
        #  * Returned: R2000,D2000 dp J2000.0 FK5 RA,Dec (rad)
        #  * 
        #  * Notes:
        #  * 
        #  * 1) The epoch BEPOCH is strictly speaking Besselian, but if a Julian epoch is
        #  * supplied the result will be affected only to a negligible extent.
        #  * 
        #  * 2) Conversion from Besselian epoch 1950.0 to Julian epoch 2000.0 only is
        #  * provided for. Conversions involving other epochs will require use of the
        #  * appropriate precession, proper motion, and E-terms routines before and/or
        #  * after FK45Z is called.
        #  * 
        #  * 3) In the FK4 catalogue the proper motions of stars within 10 degrees of the
        #  * poles do not embody the differential E-term effect and should, strictly
        #  * speaking, be handled in a different manner from stars outside these regions.
        #  * However, given the general lack of homogeneity of the star data available for
        #  * routine astrometry, the difficulties of handling positions that may have been
        #  * determined from astrometric fields spanning the polar and non-polar regions,
        #  * the likelihood that the differential E-terms effect was not taken into
        #  * account when allowing for proper motion in past astrometry, and the
        #  * undesirability of a discontinuity in the algorithm, the decision has been
        #  * made in this routine to include the effect of differential E-terms on the
        #  * proper motions for all stars, whether polar or not. At epoch 2000, and
        #  * measuring on the sky rather than in terms of dRA, the errors resulting from
        #  * this simplification are less than 1 milliarcsecond in position and 1
        #  * milliarcsecond per century in proper motion.
        #  * 
        #  * References:
        #  * 
        #  * 1 Aoki,S., et al, 1983. Astron.Astrophys., 128, 263.
        #  * 
        #  * 2 Smith, C.A. et al, 1989. "The transformation of astrometric catalog systems
        #  * to the equinox J2000.0". Astron.J. 97, 265.
        #  * 
        #  * 3 Yallop, B.D. et al, 1989. "Transformation of mean star places from FK4
        #  * B1950.0 to FK5 J2000.0 using matrices in 6-space". Astron.J. 97, 274.
        #  * 
        #  * 4 Seidelmann, P.K. (ed), 1992. "Explanatory Supplement to the Astronomical
        #  * Almanac", ISBN 0-935702-68-7.
        #  * 
        #  * Called: sla_DCS2C, sla_EPJ, sla_EPB2D, sla_DCC2S, sla_DRANRM
        #  
        # 
        #  * * Transformation from J2000.0 equatorial coordinates to IAU 1958 galactic
        #  * coordinates (double precision)
        #  * 
        #  * Given: DR,DD dp J2000.0 RA,Dec
        #  * 
        #  * Returned: DL,DB dp galactic longitude and latitude L2,B2
        #  * 
        #  * (all arguments are radians)
        #  * 
        #  * Called: sla_DCS2C, sla_DMXV, sla_DCC2S, sla_DRANRM, sla_DRANGE
        #  * 
        #  * Note: The equatorial coordinates are J2000.0. Use the routine sla_EG50 if
        #  * conversion from B1950.0 'FK4' coordinates is required.
        #  
        # 
        #  * * Performs the 3-D forward unitary transformation:
        #  * 
        #  * vector VB = matrix DM * vector VA
        #  * 
        #  * (double precision)
        #  * 
        #  * Given: DM dp(3,3) matrix VA dp(3) vector
        #  * 
        #  * Returned: VB dp(3) result vector
        #  *  
        #  
        # 
        #  * * Projection of spherical coordinates onto tangent plane: "gnomonic"
        #  * projection - "standard coordinates" (double precision)
        #  * 
        #  * Given: RA,DEC dp spherical coordinates of point to be projected RAZ,DECZ dp
        #  * spherical coordinates of tangent point
        #  * 
        #  * Returned: XI,ETA dp rectangular coordinates on tangent plane J int status: 0 =
        #  * OK, star on tangent plane 1 = error, star too far from axis 2 = error,
        #  * antistar on tangent plane 3 = error, antistar too far from axis
        #  *  
        #  
        # XI
        # ETA
        # 
        #      * + - - - - - - D T P 2 S - - - - - -
        #      * 
        #      * Transform tangent plane coordinates into spherical (double precision)
        #      * 
        #      * Given: XI,ETA dp tangent plane rectangular coordinates RAZ,DECZ dp
        #      * spherical coordinates of tangent point
        #      * 
        #      * Returned: RA,DEC dp spherical coordinates (0-2pi,+/-pi/2)
        #      * 
        #      * Called: sla_DRANRM
        #      * 
        #      * P.T.Wallace Starlink 24 July 1995
        #      *  
        #      
        # 
        #  * * Conversion of Besselian Epoch to Modified Julian Date (double precision)
        #  * 
        #  * Given: EPB dp Besselian Epoch
        #  * 
        #  * The result is the Modified Julian Date (JD - 2400000.5).
        #  
        # 
        #  * * Conversion of Modified Julian Date to Julian Epoch (double precision)
        #  * 
        #  * Given: DATE dp Modified Julian Date (JD - 2400000.5)
        #  * 
        #  * The result is the Julian Epoch.
        #  
        # 
        #  * * Normalize angle into range 0-2 pi (double precision)
        #  * 
        #  * Given: ANGLE dp the angle in radians
        #  * 
        #  * The result is ANGLE expressed in the range 0-2 pi (double precision).
        #  
        # 
        #  * * Normalize angle into range +/- pi (double precision)
        #  * 
        #  * Given: ANGLE dp the angle in radians
        #  * 
        #  * The result (double precision) is ANGLE expressed in the range +/- pi.
        #  
        # 
        #     *+
        #     *     - - - - - -
        #     *      G A L E Q
        #     *     - - - - - -
        #     *
        #     *  Transformation from IAU 1958 galactic coordinates to
        #     *  J2000.0 equatorial coordinates (double precision)
        #     *
        #     *  Given:
        #     *     DL,DB       dp       galactic longitude and latitude L2,B2
        #     *
        #     *  Returned:
        #     *     DR,DD       dp       J2000.0 RA,Dec
        #     *
        #     *  (all arguments are radians)
        #     *
        #     *  Called:
        #     *     sla_DCS2C, sla_DIMXV, sla_DCC2S, sla_DRANRM, sla_DRANGE
        #     *
        #     *  Note:
        #     *     The equatorial coordinates are J2000.0.  Use the routine
        #     *     sla_GE50 if conversion to B1950.0 'FK4' coordinates is
        #     *     required.
        #     *
        #     *  Reference:
        #     *     Blaauw et al, Mon.Not.R.Astron.Soc.,121,123 (1960)
        #     *
        #     *  P.T.Wallace   Starlink   21 September 1998
        #     *
        #     *  Copyright (C) 1998 Rutherford Appleton Laboratory
        #     *
        #     *
        #     *  L2,B2 system of galactic coordinates
        #     *
        #     *  P = 192.25       RA of galactic north pole (mean B1950.0)
        #     *  Q =  62.6        inclination of galactic to mean B1950.0 equator
        #     *  R =  33          longitude of ascending node
        #     *
        #     *  P,Q,R are degrees
        #     *
        #     *  Equatorial to galactic rotation matrix (J2000.0), obtained by
        #     *  applying the standard FK4 to FK5 transformation, for zero proper
        #     *  motion in FK5, to the columns of the B1950 equatorial to
        #     *  galactic rotation matrix:
        #     *
        #     
        #   Spherical to Cartesian
        #   Galactic to equatorial        
        #   Cartesian to spherical
        #   Express in conventional ranges
        # 
        #        * *+
        # *     - - - - - -
        # *      D I M X V
        # *     - - - - - -
        # *
        # *  Performs the 3-D backward unitary transformation:
        # *
        # *     vector VB = (inverse of matrix DM) * vector VA
        # *
        # *  (double precision)
        # *
        # *  (n.b.  the matrix must be unitary, as this routine assumes that
        # *   the inverse and transpose are identical)
        # *
        # *  Given:
        # *     DM       dp(3,3)    matrix
        # *     VA       dp(3)      vector
        # *
        # *  Returned:
        # *     VB       dp(3)      result vector
        # * 
        # 
        J = 0
        while J < 3:
            #  Static SLALIB like routines using Eclipse
            # 
            #          * *+
            # *     - - - - -
            # *      D S E P
            # *     - - - - -
            # *
            # *  Angle between two points on a sphere.
            # *
            # *  (double precision)
            # *
            # *  Given:
            # *     A1,B1    d     spherical coordinates of one point
            # *     A2,B2    d     spherical coordinates of the other point
            # *
            # *  (The spherical coordinates are [RA,Dec], [Long,Lat] etc, in radians.)
            # *
            # *  The result is the angle, in radians, between the two points.  It
            # *  is always positive.
            # *
            # *  Called:  sla_DCS2C, sla_DSEPV
            # *
            # *  Last revision:   7 May 2000
            # *
            # *  Copyright P.T.Wallace.  All rights reserved.
            # *
            # *-
            #          
            #   Convert coordinates from spherical to Cartesian.
            # 
            #          * *+
            # *     - - - - - -
            # *      D S E P V
            # *     - - - - - -
            # *
            # *  Angle between two vectors.
            # *
            # *  (double precision)
            # *
            # *  Given:
            # *     V1      d(3)    first vector
            # *     V2      d(3)    second vector
            # *
            # *  The result is the angle, in radians, between the two vectors.  It
            # *  is always positive.
            # *
            # *  Notes:
            # *
            # *  1  There is no requirement for the vectors to be unit length.
            # *
            # *  2  If either vector is null, zero is returned.
            # *
            # *  3  The simplest formulation would use dot product alone.  However,
            # *     this would reduce the accuracy for angles near zero and pi.  The
            # *     algorithm uses both cross product and dot product, which maintains
            # *     accuracy for all sizes of angle.
            # *
            # *  Called:  sla_DVXV, sla_DVN, sla_DVDV
            #          
            # sla_DVDV;
            #  Wv[3] is S
            # *  Dot product = cosine multiplied by the two moduli.
            # *  Angle between the vectors.
            # 
            #          * *+
            # *     - - - - -
            # *      D V D V
            # *     - - - - -
            # *
            # *  Scalar product of two 3-vectors  (double precision)
            # *
            # *  Given:
            # *      VA      dp(3)     first vector
            # *      VB      dp(3)     second vector
            # *
            # *  The result is the scalar product VA.VB (double precision)
            # *
            # *  P.T.Wallace   Starlink   November 1984
            # *
            # *  Copyright (C) 1995 Rutherford Appleton Laboratory
            # *
            #          
            # 
            #          * *+
            # *     - - - -
            # *      D V N
            # *     - - - -
            # *
            # *  Normalizes a 3-vector also giving the modulus (double precision)
            # *
            # *  Given:
            # *     V       dp(3)      vector
            # *
            # *  Returned:
            # *     UV      dp(3)      unit vector in direction of V
            # *     VM      dp         modulus of V
            # *
            # *  If the modulus of V is zero, UV is set to zero as well
            # *
            # *  P.T.Wallace   Starlink   23 November 1995
            #          
            #  4 not 3 
            # *  Modulus
            #  *  Normalize the vector
            # 
            #          * *+
            # *     - - - - -
            # *      D V X V
            # *     - - - - -
            # *
            # *  Vector product of two 3-vectors  (double precision)
            # *
            # *  Given:
            # *      VA      dp(3)     first vector
            # *      VB      dp(3)     second vector
            # *
            # *  Returned:
            # *      VC      dp(3)     vector result
            # *
            # *  P.T.Wallace   Starlink   March 1986
            # *
            # *  Copyright (C) 1995 Rutherford Appleton Laboratory
            # *
            #          
            # *  Form the vector product VA cross VB
            # *  Return the result
            # 
            #  * * Spherical coordinates to direction cosines (double precision)
            #  * 
            #  * Given: A,B dp spherical coordinates in radians (RA,Dec), (Long,Lat) etc
            #  * 
            #  * Returned: V dp(3) x,y,z unit vector
            #  * 
            #  * The spherical coordinates are longitude (+ve anticlockwise looking from the
            #  * +ve latitude pole) and latitude. The Cartesian coordinates are right handed,
            #  * with the x axis at zero longitude and latitude, and the z axis at the +ve
            #  * latitude pole.
            #  *  
            #  
            # 
            #  * * Direction cosines to spherical coordinates (double precision)
            #  * 
            #  * Given: V d(3) x,y,z vector
            #  * 
            #  * Returned: A,B d spherical coordinates in radians
            #  * 
            #  * The spherical coordinates are longitude (+ve anticlockwise looking from the
            #  * +ve latitude pole) and latitude. The Cartesian coordinates are right handed,
            #  * with the x axis at zero longitude and latitude, and the z axis at the +ve
            #  * latitude pole.
            #  * 
            #  * If V is null, zero A and B are returned. At either pole, zero A is returned.
            #  *  
            #  
            # 
            #  * * Convert B1950.0 FK4 star data to J2000.0 FK5 assuming zero proper motion in
            #  * the FK5 frame (double precision)
            #  * 
            #  * This routine converts stars from the old, Bessel-Newcomb, FK4 system to the
            #  * new, IAU 1976, FK5, Fricke system, in such a way that the FK5 proper motion
            #  * is zero. Because such a star has, in general, a non-zero proper motion in the
            #  * FK4 system, the routine requires the epoch at which the position in the FK4
            #  * system was determined.
            #  * 
            #  * The method is from Appendix 2 of Ref 1, but using the constants of Ref 4.
            #  * 
            #  * Given: R1950,D1950 dp B1950.0 FK4 RA,Dec at epoch (rad) BEPOCH dp Besselian
            #  * epoch (e.g. 1979.3D0)
            #  * 
            #  * Returned: R2000,D2000 dp J2000.0 FK5 RA,Dec (rad)
            #  * 
            #  * Notes:
            #  * 
            #  * 1) The epoch BEPOCH is strictly speaking Besselian, but if a Julian epoch is
            #  * supplied the result will be affected only to a negligible extent.
            #  * 
            #  * 2) Conversion from Besselian epoch 1950.0 to Julian epoch 2000.0 only is
            #  * provided for. Conversions involving other epochs will require use of the
            #  * appropriate precession, proper motion, and E-terms routines before and/or
            #  * after FK45Z is called.
            #  * 
            #  * 3) In the FK4 catalogue the proper motions of stars within 10 degrees of the
            #  * poles do not embody the differential E-term effect and should, strictly
            #  * speaking, be handled in a different manner from stars outside these regions.
            #  * However, given the general lack of homogeneity of the star data available for
            #  * routine astrometry, the difficulties of handling positions that may have been
            #  * determined from astrometric fields spanning the polar and non-polar regions,
            #  * the likelihood that the differential E-terms effect was not taken into
            #  * account when allowing for proper motion in past astrometry, and the
            #  * undesirability of a discontinuity in the algorithm, the decision has been
            #  * made in this routine to include the effect of differential E-terms on the
            #  * proper motions for all stars, whether polar or not. At epoch 2000, and
            #  * measuring on the sky rather than in terms of dRA, the errors resulting from
            #  * this simplification are less than 1 milliarcsecond in position and 1
            #  * milliarcsecond per century in proper motion.
            #  * 
            #  * References:
            #  * 
            #  * 1 Aoki,S., et al, 1983. Astron.Astrophys., 128, 263.
            #  * 
            #  * 2 Smith, C.A. et al, 1989. "The transformation of astrometric catalog systems
            #  * to the equinox J2000.0". Astron.J. 97, 265.
            #  * 
            #  * 3 Yallop, B.D. et al, 1989. "Transformation of mean star places from FK4
            #  * B1950.0 to FK5 J2000.0 using matrices in 6-space". Astron.J. 97, 274.
            #  * 
            #  * 4 Seidelmann, P.K. (ed), 1992. "Explanatory Supplement to the Astronomical
            #  * Almanac", ISBN 0-935702-68-7.
            #  * 
            #  * Called: sla_DCS2C, sla_EPJ, sla_EPB2D, sla_DCC2S, sla_DRANRM
            #  
            # 
            #  * * Transformation from J2000.0 equatorial coordinates to IAU 1958 galactic
            #  * coordinates (double precision)
            #  * 
            #  * Given: DR,DD dp J2000.0 RA,Dec
            #  * 
            #  * Returned: DL,DB dp galactic longitude and latitude L2,B2
            #  * 
            #  * (all arguments are radians)
            #  * 
            #  * Called: sla_DCS2C, sla_DMXV, sla_DCC2S, sla_DRANRM, sla_DRANGE
            #  * 
            #  * Note: The equatorial coordinates are J2000.0. Use the routine sla_EG50 if
            #  * conversion from B1950.0 'FK4' coordinates is required.
            #  
            # 
            #  * * Performs the 3-D forward unitary transformation:
            #  * 
            #  * vector VB = matrix DM * vector VA
            #  * 
            #  * (double precision)
            #  * 
            #  * Given: DM dp(3,3) matrix VA dp(3) vector
            #  * 
            #  * Returned: VB dp(3) result vector
            #  *  
            #  
            # 
            #  * * Projection of spherical coordinates onto tangent plane: "gnomonic"
            #  * projection - "standard coordinates" (double precision)
            #  * 
            #  * Given: RA,DEC dp spherical coordinates of point to be projected RAZ,DECZ dp
            #  * spherical coordinates of tangent point
            #  * 
            #  * Returned: XI,ETA dp rectangular coordinates on tangent plane J int status: 0 =
            #  * OK, star on tangent plane 1 = error, star too far from axis 2 = error,
            #  * antistar on tangent plane 3 = error, antistar too far from axis
            #  *  
            #  
            # XI
            # ETA
            # 
            #      * + - - - - - - D T P 2 S - - - - - -
            #      * 
            #      * Transform tangent plane coordinates into spherical (double precision)
            #      * 
            #      * Given: XI,ETA dp tangent plane rectangular coordinates RAZ,DECZ dp
            #      * spherical coordinates of tangent point
            #      * 
            #      * Returned: RA,DEC dp spherical coordinates (0-2pi,+/-pi/2)
            #      * 
            #      * Called: sla_DRANRM
            #      * 
            #      * P.T.Wallace Starlink 24 July 1995
            #      *  
            #      
            # 
            #  * * Conversion of Besselian Epoch to Modified Julian Date (double precision)
            #  * 
            #  * Given: EPB dp Besselian Epoch
            #  * 
            #  * The result is the Modified Julian Date (JD - 2400000.5).
            #  
            # 
            #  * * Conversion of Modified Julian Date to Julian Epoch (double precision)
            #  * 
            #  * Given: DATE dp Modified Julian Date (JD - 2400000.5)
            #  * 
            #  * The result is the Julian Epoch.
            #  
            # 
            #  * * Normalize angle into range 0-2 pi (double precision)
            #  * 
            #  * Given: ANGLE dp the angle in radians
            #  * 
            #  * The result is ANGLE expressed in the range 0-2 pi (double precision).
            #  
            # 
            #  * * Normalize angle into range +/- pi (double precision)
            #  * 
            #  * Given: ANGLE dp the angle in radians
            #  * 
            #  * The result (double precision) is ANGLE expressed in the range +/- pi.
            #  
            # 
            #     *+
            #     *     - - - - - -
            #     *      G A L E Q
            #     *     - - - - - -
            #     *
            #     *  Transformation from IAU 1958 galactic coordinates to
            #     *  J2000.0 equatorial coordinates (double precision)
            #     *
            #     *  Given:
            #     *     DL,DB       dp       galactic longitude and latitude L2,B2
            #     *
            #     *  Returned:
            #     *     DR,DD       dp       J2000.0 RA,Dec
            #     *
            #     *  (all arguments are radians)
            #     *
            #     *  Called:
            #     *     sla_DCS2C, sla_DIMXV, sla_DCC2S, sla_DRANRM, sla_DRANGE
            #     *
            #     *  Note:
            #     *     The equatorial coordinates are J2000.0.  Use the routine
            #     *     sla_GE50 if conversion to B1950.0 'FK4' coordinates is
            #     *     required.
            #     *
            #     *  Reference:
            #     *     Blaauw et al, Mon.Not.R.Astron.Soc.,121,123 (1960)
            #     *
            #     *  P.T.Wallace   Starlink   21 September 1998
            #     *
            #     *  Copyright (C) 1998 Rutherford Appleton Laboratory
            #     *
            #     *
            #     *  L2,B2 system of galactic coordinates
            #     *
            #     *  P = 192.25       RA of galactic north pole (mean B1950.0)
            #     *  Q =  62.6        inclination of galactic to mean B1950.0 equator
            #     *  R =  33          longitude of ascending node
            #     *
            #     *  P,Q,R are degrees
            #     *
            #     *  Equatorial to galactic rotation matrix (J2000.0), obtained by
            #     *  applying the standard FK4 to FK5 transformation, for zero proper
            #     *  motion in FK5, to the columns of the B1950 equatorial to
            #     *  galactic rotation matrix:
            #     *
            #     
            #   Spherical to Cartesian
            #   Galactic to equatorial        
            #   Cartesian to spherical
            #   Express in conventional ranges
            # 
            #        * *+
            # *     - - - - - -
            # *      D I M X V
            # *     - - - - - -
            # *
            # *  Performs the 3-D backward unitary transformation:
            # *
            # *     vector VB = (inverse of matrix DM) * vector VA
            # *
            # *  (double precision)
            # *
            # *  (n.b.  the matrix must be unitary, as this routine assumes that
            # *   the inverse and transpose are identical)
            # *
            # *  Given:
            # *     DM       dp(3,3)    matrix
            # *     VA       dp(3)      vector
            # *
            # *  Returned:
            # *     VB       dp(3)      result vector
            # * 
            # 
            W = 0.0
            #  Static SLALIB like routines using Eclipse
            # 
            #          * *+
            # *     - - - - -
            # *      D S E P
            # *     - - - - -
            # *
            # *  Angle between two points on a sphere.
            # *
            # *  (double precision)
            # *
            # *  Given:
            # *     A1,B1    d     spherical coordinates of one point
            # *     A2,B2    d     spherical coordinates of the other point
            # *
            # *  (The spherical coordinates are [RA,Dec], [Long,Lat] etc, in radians.)
            # *
            # *  The result is the angle, in radians, between the two points.  It
            # *  is always positive.
            # *
            # *  Called:  sla_DCS2C, sla_DSEPV
            # *
            # *  Last revision:   7 May 2000
            # *
            # *  Copyright P.T.Wallace.  All rights reserved.
            # *
            # *-
            #          
            #   Convert coordinates from spherical to Cartesian.
            # 
            #          * *+
            # *     - - - - - -
            # *      D S E P V
            # *     - - - - - -
            # *
            # *  Angle between two vectors.
            # *
            # *  (double precision)
            # *
            # *  Given:
            # *     V1      d(3)    first vector
            # *     V2      d(3)    second vector
            # *
            # *  The result is the angle, in radians, between the two vectors.  It
            # *  is always positive.
            # *
            # *  Notes:
            # *
            # *  1  There is no requirement for the vectors to be unit length.
            # *
            # *  2  If either vector is null, zero is returned.
            # *
            # *  3  The simplest formulation would use dot product alone.  However,
            # *     this would reduce the accuracy for angles near zero and pi.  The
            # *     algorithm uses both cross product and dot product, which maintains
            # *     accuracy for all sizes of angle.
            # *
            # *  Called:  sla_DVXV, sla_DVN, sla_DVDV
            #          
            # sla_DVDV;
            #  Wv[3] is S
            # *  Dot product = cosine multiplied by the two moduli.
            # *  Angle between the vectors.
            # 
            #          * *+
            # *     - - - - -
            # *      D V D V
            # *     - - - - -
            # *
            # *  Scalar product of two 3-vectors  (double precision)
            # *
            # *  Given:
            # *      VA      dp(3)     first vector
            # *      VB      dp(3)     second vector
            # *
            # *  The result is the scalar product VA.VB (double precision)
            # *
            # *  P.T.Wallace   Starlink   November 1984
            # *
            # *  Copyright (C) 1995 Rutherford Appleton Laboratory
            # *
            #          
            # 
            #          * *+
            # *     - - - -
            # *      D V N
            # *     - - - -
            # *
            # *  Normalizes a 3-vector also giving the modulus (double precision)
            # *
            # *  Given:
            # *     V       dp(3)      vector
            # *
            # *  Returned:
            # *     UV      dp(3)      unit vector in direction of V
            # *     VM      dp         modulus of V
            # *
            # *  If the modulus of V is zero, UV is set to zero as well
            # *
            # *  P.T.Wallace   Starlink   23 November 1995
            #          
            #  4 not 3 
            # *  Modulus
            #  *  Normalize the vector
            # 
            #          * *+
            # *     - - - - -
            # *      D V X V
            # *     - - - - -
            # *
            # *  Vector product of two 3-vectors  (double precision)
            # *
            # *  Given:
            # *      VA      dp(3)     first vector
            # *      VB      dp(3)     second vector
            # *
            # *  Returned:
            # *      VC      dp(3)     vector result
            # *
            # *  P.T.Wallace   Starlink   March 1986
            # *
            # *  Copyright (C) 1995 Rutherford Appleton Laboratory
            # *
            #          
            # *  Form the vector product VA cross VB
            # *  Return the result
            # 
            #  * * Spherical coordinates to direction cosines (double precision)
            #  * 
            #  * Given: A,B dp spherical coordinates in radians (RA,Dec), (Long,Lat) etc
            #  * 
            #  * Returned: V dp(3) x,y,z unit vector
            #  * 
            #  * The spherical coordinates are longitude (+ve anticlockwise looking from the
            #  * +ve latitude pole) and latitude. The Cartesian coordinates are right handed,
            #  * with the x axis at zero longitude and latitude, and the z axis at the +ve
            #  * latitude pole.
            #  *  
            #  
            # 
            #  * * Direction cosines to spherical coordinates (double precision)
            #  * 
            #  * Given: V d(3) x,y,z vector
            #  * 
            #  * Returned: A,B d spherical coordinates in radians
            #  * 
            #  * The spherical coordinates are longitude (+ve anticlockwise looking from the
            #  * +ve latitude pole) and latitude. The Cartesian coordinates are right handed,
            #  * with the x axis at zero longitude and latitude, and the z axis at the +ve
            #  * latitude pole.
            #  * 
            #  * If V is null, zero A and B are returned. At either pole, zero A is returned.
            #  *  
            #  
            # 
            #  * * Convert B1950.0 FK4 star data to J2000.0 FK5 assuming zero proper motion in
            #  * the FK5 frame (double precision)
            #  * 
            #  * This routine converts stars from the old, Bessel-Newcomb, FK4 system to the
            #  * new, IAU 1976, FK5, Fricke system, in such a way that the FK5 proper motion
            #  * is zero. Because such a star has, in general, a non-zero proper motion in the
            #  * FK4 system, the routine requires the epoch at which the position in the FK4
            #  * system was determined.
            #  * 
            #  * The method is from Appendix 2 of Ref 1, but using the constants of Ref 4.
            #  * 
            #  * Given: R1950,D1950 dp B1950.0 FK4 RA,Dec at epoch (rad) BEPOCH dp Besselian
            #  * epoch (e.g. 1979.3D0)
            #  * 
            #  * Returned: R2000,D2000 dp J2000.0 FK5 RA,Dec (rad)
            #  * 
            #  * Notes:
            #  * 
            #  * 1) The epoch BEPOCH is strictly speaking Besselian, but if a Julian epoch is
            #  * supplied the result will be affected only to a negligible extent.
            #  * 
            #  * 2) Conversion from Besselian epoch 1950.0 to Julian epoch 2000.0 only is
            #  * provided for. Conversions involving other epochs will require use of the
            #  * appropriate precession, proper motion, and E-terms routines before and/or
            #  * after FK45Z is called.
            #  * 
            #  * 3) In the FK4 catalogue the proper motions of stars within 10 degrees of the
            #  * poles do not embody the differential E-term effect and should, strictly
            #  * speaking, be handled in a different manner from stars outside these regions.
            #  * However, given the general lack of homogeneity of the star data available for
            #  * routine astrometry, the difficulties of handling positions that may have been
            #  * determined from astrometric fields spanning the polar and non-polar regions,
            #  * the likelihood that the differential E-terms effect was not taken into
            #  * account when allowing for proper motion in past astrometry, and the
            #  * undesirability of a discontinuity in the algorithm, the decision has been
            #  * made in this routine to include the effect of differential E-terms on the
            #  * proper motions for all stars, whether polar or not. At epoch 2000, and
            #  * measuring on the sky rather than in terms of dRA, the errors resulting from
            #  * this simplification are less than 1 milliarcsecond in position and 1
            #  * milliarcsecond per century in proper motion.
            #  * 
            #  * References:
            #  * 
            #  * 1 Aoki,S., et al, 1983. Astron.Astrophys., 128, 263.
            #  * 
            #  * 2 Smith, C.A. et al, 1989. "The transformation of astrometric catalog systems
            #  * to the equinox J2000.0". Astron.J. 97, 265.
            #  * 
            #  * 3 Yallop, B.D. et al, 1989. "Transformation of mean star places from FK4
            #  * B1950.0 to FK5 J2000.0 using matrices in 6-space". Astron.J. 97, 274.
            #  * 
            #  * 4 Seidelmann, P.K. (ed), 1992. "Explanatory Supplement to the Astronomical
            #  * Almanac", ISBN 0-935702-68-7.
            #  * 
            #  * Called: sla_DCS2C, sla_EPJ, sla_EPB2D, sla_DCC2S, sla_DRANRM
            #  
            # 
            #  * * Transformation from J2000.0 equatorial coordinates to IAU 1958 galactic
            #  * coordinates (double precision)
            #  * 
            #  * Given: DR,DD dp J2000.0 RA,Dec
            #  * 
            #  * Returned: DL,DB dp galactic longitude and latitude L2,B2
            #  * 
            #  * (all arguments are radians)
            #  * 
            #  * Called: sla_DCS2C, sla_DMXV, sla_DCC2S, sla_DRANRM, sla_DRANGE
            #  * 
            #  * Note: The equatorial coordinates are J2000.0. Use the routine sla_EG50 if
            #  * conversion from B1950.0 'FK4' coordinates is required.
            #  
            # 
            #  * * Performs the 3-D forward unitary transformation:
            #  * 
            #  * vector VB = matrix DM * vector VA
            #  * 
            #  * (double precision)
            #  * 
            #  * Given: DM dp(3,3) matrix VA dp(3) vector
            #  * 
            #  * Returned: VB dp(3) result vector
            #  *  
            #  
            # 
            #  * * Projection of spherical coordinates onto tangent plane: "gnomonic"
            #  * projection - "standard coordinates" (double precision)
            #  * 
            #  * Given: RA,DEC dp spherical coordinates of point to be projected RAZ,DECZ dp
            #  * spherical coordinates of tangent point
            #  * 
            #  * Returned: XI,ETA dp rectangular coordinates on tangent plane J int status: 0 =
            #  * OK, star on tangent plane 1 = error, star too far from axis 2 = error,
            #  * antistar on tangent plane 3 = error, antistar too far from axis
            #  *  
            #  
            # XI
            # ETA
            # 
            #      * + - - - - - - D T P 2 S - - - - - -
            #      * 
            #      * Transform tangent plane coordinates into spherical (double precision)
            #      * 
            #      * Given: XI,ETA dp tangent plane rectangular coordinates RAZ,DECZ dp
            #      * spherical coordinates of tangent point
            #      * 
            #      * Returned: RA,DEC dp spherical coordinates (0-2pi,+/-pi/2)
            #      * 
            #      * Called: sla_DRANRM
            #      * 
            #      * P.T.Wallace Starlink 24 July 1995
            #      *  
            #      
            # 
            #  * * Conversion of Besselian Epoch to Modified Julian Date (double precision)
            #  * 
            #  * Given: EPB dp Besselian Epoch
            #  * 
            #  * The result is the Modified Julian Date (JD - 2400000.5).
            #  
            # 
            #  * * Conversion of Modified Julian Date to Julian Epoch (double precision)
            #  * 
            #  * Given: DATE dp Modified Julian Date (JD - 2400000.5)
            #  * 
            #  * The result is the Julian Epoch.
            #  
            # 
            #  * * Normalize angle into range 0-2 pi (double precision)
            #  * 
            #  * Given: ANGLE dp the angle in radians
            #  * 
            #  * The result is ANGLE expressed in the range 0-2 pi (double precision).
            #  
            # 
            #  * * Normalize angle into range +/- pi (double precision)
            #  * 
            #  * Given: ANGLE dp the angle in radians
            #  * 
            #  * The result (double precision) is ANGLE expressed in the range +/- pi.
            #  
            # 
            #     *+
            #     *     - - - - - -
            #     *      G A L E Q
            #     *     - - - - - -
            #     *
            #     *  Transformation from IAU 1958 galactic coordinates to
            #     *  J2000.0 equatorial coordinates (double precision)
            #     *
            #     *  Given:
            #     *     DL,DB       dp       galactic longitude and latitude L2,B2
            #     *
            #     *  Returned:
            #     *     DR,DD       dp       J2000.0 RA,Dec
            #     *
            #     *  (all arguments are radians)
            #     *
            #     *  Called:
            #     *     sla_DCS2C, sla_DIMXV, sla_DCC2S, sla_DRANRM, sla_DRANGE
            #     *
            #     *  Note:
            #     *     The equatorial coordinates are J2000.0.  Use the routine
            #     *     sla_GE50 if conversion to B1950.0 'FK4' coordinates is
            #     *     required.
            #     *
            #     *  Reference:
            #     *     Blaauw et al, Mon.Not.R.Astron.Soc.,121,123 (1960)
            #     *
            #     *  P.T.Wallace   Starlink   21 September 1998
            #     *
            #     *  Copyright (C) 1998 Rutherford Appleton Laboratory
            #     *
            #     *
            #     *  L2,B2 system of galactic coordinates
            #     *
            #     *  P = 192.25       RA of galactic north pole (mean B1950.0)
            #     *  Q =  62.6        inclination of galactic to mean B1950.0 equator
            #     *  R =  33          longitude of ascending node
            #     *
            #     *  P,Q,R are degrees
            #     *
            #     *  Equatorial to galactic rotation matrix (J2000.0), obtained by
            #     *  applying the standard FK4 to FK5 transformation, for zero proper
            #     *  motion in FK5, to the columns of the B1950 equatorial to
            #     *  galactic rotation matrix:
            #     *
            #     
            #   Spherical to Cartesian
            #   Galactic to equatorial        
            #   Cartesian to spherical
            #   Express in conventional ranges
            # 
            #        * *+
            # *     - - - - - -
            # *      D I M X V
            # *     - - - - - -
            # *
            # *  Performs the 3-D backward unitary transformation:
            # *
            # *     vector VB = (inverse of matrix DM) * vector VA
            # *
            # *  (double precision)
            # *
            # *  (n.b.  the matrix must be unitary, as this routine assumes that
            # *   the inverse and transpose are identical)
            # *
            # *  Given:
            # *     DM       dp(3,3)    matrix
            # *     VA       dp(3)      vector
            # *
            # *  Returned:
            # *     VB       dp(3)      result vector
            # * 
            # 
            I=0
            while I < 3:
                #  Static SLALIB like routines using Eclipse
                # 
                #          * *+
                # *     - - - - -
                # *      D S E P
                # *     - - - - -
                # *
                # *  Angle between two points on a sphere.
                # *
                # *  (double precision)
                # *
                # *  Given:
                # *     A1,B1    d     spherical coordinates of one point
                # *     A2,B2    d     spherical coordinates of the other point
                # *
                # *  (The spherical coordinates are [RA,Dec], [Long,Lat] etc, in radians.)
                # *
                # *  The result is the angle, in radians, between the two points.  It
                # *  is always positive.
                # *
                # *  Called:  sla_DCS2C, sla_DSEPV
                # *
                # *  Last revision:   7 May 2000
                # *
                # *  Copyright P.T.Wallace.  All rights reserved.
                # *
                # *-
                #          
                #   Convert coordinates from spherical to Cartesian.
                # 
                #          * *+
                # *     - - - - - -
                # *      D S E P V
                # *     - - - - - -
                # *
                # *  Angle between two vectors.
                # *
                # *  (double precision)
                # *
                # *  Given:
                # *     V1      d(3)    first vector
                # *     V2      d(3)    second vector
                # *
                # *  The result is the angle, in radians, between the two vectors.  It
                # *  is always positive.
                # *
                # *  Notes:
                # *
                # *  1  There is no requirement for the vectors to be unit length.
                # *
                # *  2  If either vector is null, zero is returned.
                # *
                # *  3  The simplest formulation would use dot product alone.  However,
                # *     this would reduce the accuracy for angles near zero and pi.  The
                # *     algorithm uses both cross product and dot product, which maintains
                # *     accuracy for all sizes of angle.
                # *
                # *  Called:  sla_DVXV, sla_DVN, sla_DVDV
                #          
                # sla_DVDV;
                #  Wv[3] is S
                # *  Dot product = cosine multiplied by the two moduli.
                # *  Angle between the vectors.
                # 
                #          * *+
                # *     - - - - -
                # *      D V D V
                # *     - - - - -
                # *
                # *  Scalar product of two 3-vectors  (double precision)
                # *
                # *  Given:
                # *      VA      dp(3)     first vector
                # *      VB      dp(3)     second vector
                # *
                # *  The result is the scalar product VA.VB (double precision)
                # *
                # *  P.T.Wallace   Starlink   November 1984
                # *
                # *  Copyright (C) 1995 Rutherford Appleton Laboratory
                # *
                #          
                # 
                #          * *+
                # *     - - - -
                # *      D V N
                # *     - - - -
                # *
                # *  Normalizes a 3-vector also giving the modulus (double precision)
                # *
                # *  Given:
                # *     V       dp(3)      vector
                # *
                # *  Returned:
                # *     UV      dp(3)      unit vector in direction of V
                # *     VM      dp         modulus of V
                # *
                # *  If the modulus of V is zero, UV is set to zero as well
                # *
                # *  P.T.Wallace   Starlink   23 November 1995
                #          
                #  4 not 3 
                # *  Modulus
                #  *  Normalize the vector
                # 
                #          * *+
                # *     - - - - -
                # *      D V X V
                # *     - - - - -
                # *
                # *  Vector product of two 3-vectors  (double precision)
                # *
                # *  Given:
                # *      VA      dp(3)     first vector
                # *      VB      dp(3)     second vector
                # *
                # *  Returned:
                # *      VC      dp(3)     vector result
                # *
                # *  P.T.Wallace   Starlink   March 1986
                # *
                # *  Copyright (C) 1995 Rutherford Appleton Laboratory
                # *
                #          
                # *  Form the vector product VA cross VB
                # *  Return the result
                # 
                #  * * Spherical coordinates to direction cosines (double precision)
                #  * 
                #  * Given: A,B dp spherical coordinates in radians (RA,Dec), (Long,Lat) etc
                #  * 
                #  * Returned: V dp(3) x,y,z unit vector
                #  * 
                #  * The spherical coordinates are longitude (+ve anticlockwise looking from the
                #  * +ve latitude pole) and latitude. The Cartesian coordinates are right handed,
                #  * with the x axis at zero longitude and latitude, and the z axis at the +ve
                #  * latitude pole.
                #  *  
                #  
                # 
                #  * * Direction cosines to spherical coordinates (double precision)
                #  * 
                #  * Given: V d(3) x,y,z vector
                #  * 
                #  * Returned: A,B d spherical coordinates in radians
                #  * 
                #  * The spherical coordinates are longitude (+ve anticlockwise looking from the
                #  * +ve latitude pole) and latitude. The Cartesian coordinates are right handed,
                #  * with the x axis at zero longitude and latitude, and the z axis at the +ve
                #  * latitude pole.
                #  * 
                #  * If V is null, zero A and B are returned. At either pole, zero A is returned.
                #  *  
                #  
                # 
                #  * * Convert B1950.0 FK4 star data to J2000.0 FK5 assuming zero proper motion in
                #  * the FK5 frame (double precision)
                #  * 
                #  * This routine converts stars from the old, Bessel-Newcomb, FK4 system to the
                #  * new, IAU 1976, FK5, Fricke system, in such a way that the FK5 proper motion
                #  * is zero. Because such a star has, in general, a non-zero proper motion in the
                #  * FK4 system, the routine requires the epoch at which the position in the FK4
                #  * system was determined.
                #  * 
                #  * The method is from Appendix 2 of Ref 1, but using the constants of Ref 4.
                #  * 
                #  * Given: R1950,D1950 dp B1950.0 FK4 RA,Dec at epoch (rad) BEPOCH dp Besselian
                #  * epoch (e.g. 1979.3D0)
                #  * 
                #  * Returned: R2000,D2000 dp J2000.0 FK5 RA,Dec (rad)
                #  * 
                #  * Notes:
                #  * 
                #  * 1) The epoch BEPOCH is strictly speaking Besselian, but if a Julian epoch is
                #  * supplied the result will be affected only to a negligible extent.
                #  * 
                #  * 2) Conversion from Besselian epoch 1950.0 to Julian epoch 2000.0 only is
                #  * provided for. Conversions involving other epochs will require use of the
                #  * appropriate precession, proper motion, and E-terms routines before and/or
                #  * after FK45Z is called.
                #  * 
                #  * 3) In the FK4 catalogue the proper motions of stars within 10 degrees of the
                #  * poles do not embody the differential E-term effect and should, strictly
                #  * speaking, be handled in a different manner from stars outside these regions.
                #  * However, given the general lack of homogeneity of the star data available for
                #  * routine astrometry, the difficulties of handling positions that may have been
                #  * determined from astrometric fields spanning the polar and non-polar regions,
                #  * the likelihood that the differential E-terms effect was not taken into
                #  * account when allowing for proper motion in past astrometry, and the
                #  * undesirability of a discontinuity in the algorithm, the decision has been
                #  * made in this routine to include the effect of differential E-terms on the
                #  * proper motions for all stars, whether polar or not. At epoch 2000, and
                #  * measuring on the sky rather than in terms of dRA, the errors resulting from
                #  * this simplification are less than 1 milliarcsecond in position and 1
                #  * milliarcsecond per century in proper motion.
                #  * 
                #  * References:
                #  * 
                #  * 1 Aoki,S., et al, 1983. Astron.Astrophys., 128, 263.
                #  * 
                #  * 2 Smith, C.A. et al, 1989. "The transformation of astrometric catalog systems
                #  * to the equinox J2000.0". Astron.J. 97, 265.
                #  * 
                #  * 3 Yallop, B.D. et al, 1989. "Transformation of mean star places from FK4
                #  * B1950.0 to FK5 J2000.0 using matrices in 6-space". Astron.J. 97, 274.
                #  * 
                #  * 4 Seidelmann, P.K. (ed), 1992. "Explanatory Supplement to the Astronomical
                #  * Almanac", ISBN 0-935702-68-7.
                #  * 
                #  * Called: sla_DCS2C, sla_EPJ, sla_EPB2D, sla_DCC2S, sla_DRANRM
                #  
                # 
                #  * * Transformation from J2000.0 equatorial coordinates to IAU 1958 galactic
                #  * coordinates (double precision)
                #  * 
                #  * Given: DR,DD dp J2000.0 RA,Dec
                #  * 
                #  * Returned: DL,DB dp galactic longitude and latitude L2,B2
                #  * 
                #  * (all arguments are radians)
                #  * 
                #  * Called: sla_DCS2C, sla_DMXV, sla_DCC2S, sla_DRANRM, sla_DRANGE
                #  * 
                #  * Note: The equatorial coordinates are J2000.0. Use the routine sla_EG50 if
                #  * conversion from B1950.0 'FK4' coordinates is required.
                #  
                # 
                #  * * Performs the 3-D forward unitary transformation:
                #  * 
                #  * vector VB = matrix DM * vector VA
                #  * 
                #  * (double precision)
                #  * 
                #  * Given: DM dp(3,3) matrix VA dp(3) vector
                #  * 
                #  * Returned: VB dp(3) result vector
                #  *  
                #  
                # 
                #  * * Projection of spherical coordinates onto tangent plane: "gnomonic"
                #  * projection - "standard coordinates" (double precision)
                #  * 
                #  * Given: RA,DEC dp spherical coordinates of point to be projected RAZ,DECZ dp
                #  * spherical coordinates of tangent point
                #  * 
                #  * Returned: XI,ETA dp rectangular coordinates on tangent plane J int status: 0 =
                #  * OK, star on tangent plane 1 = error, star too far from axis 2 = error,
                #  * antistar on tangent plane 3 = error, antistar too far from axis
                #  *  
                #  
                # XI
                # ETA
                # 
                #      * + - - - - - - D T P 2 S - - - - - -
                #      * 
                #      * Transform tangent plane coordinates into spherical (double precision)
                #      * 
                #      * Given: XI,ETA dp tangent plane rectangular coordinates RAZ,DECZ dp
                #      * spherical coordinates of tangent point
                #      * 
                #      * Returned: RA,DEC dp spherical coordinates (0-2pi,+/-pi/2)
                #      * 
                #      * Called: sla_DRANRM
                #      * 
                #      * P.T.Wallace Starlink 24 July 1995
                #      *  
                #      
                # 
                #  * * Conversion of Besselian Epoch to Modified Julian Date (double precision)
                #  * 
                #  * Given: EPB dp Besselian Epoch
                #  * 
                #  * The result is the Modified Julian Date (JD - 2400000.5).
                #  
                # 
                #  * * Conversion of Modified Julian Date to Julian Epoch (double precision)
                #  * 
                #  * Given: DATE dp Modified Julian Date (JD - 2400000.5)
                #  * 
                #  * The result is the Julian Epoch.
                #  
                # 
                #  * * Normalize angle into range 0-2 pi (double precision)
                #  * 
                #  * Given: ANGLE dp the angle in radians
                #  * 
                #  * The result is ANGLE expressed in the range 0-2 pi (double precision).
                #  
                # 
                #  * * Normalize angle into range +/- pi (double precision)
                #  * 
                #  * Given: ANGLE dp the angle in radians
                #  * 
                #  * The result (double precision) is ANGLE expressed in the range +/- pi.
                #  
                # 
                #     *+
                #     *     - - - - - -
                #     *      G A L E Q
                #     *     - - - - - -
                #     *
                #     *  Transformation from IAU 1958 galactic coordinates to
                #     *  J2000.0 equatorial coordinates (double precision)
                #     *
                #     *  Given:
                #     *     DL,DB       dp       galactic longitude and latitude L2,B2
                #     *
                #     *  Returned:
                #     *     DR,DD       dp       J2000.0 RA,Dec
                #     *
                #     *  (all arguments are radians)
                #     *
                #     *  Called:
                #     *     sla_DCS2C, sla_DIMXV, sla_DCC2S, sla_DRANRM, sla_DRANGE
                #     *
                #     *  Note:
                #     *     The equatorial coordinates are J2000.0.  Use the routine
                #     *     sla_GE50 if conversion to B1950.0 'FK4' coordinates is
                #     *     required.
                #     *
                #     *  Reference:
                #     *     Blaauw et al, Mon.Not.R.Astron.Soc.,121,123 (1960)
                #     *
                #     *  P.T.Wallace   Starlink   21 September 1998
                #     *
                #     *  Copyright (C) 1998 Rutherford Appleton Laboratory
                #     *
                #     *
                #     *  L2,B2 system of galactic coordinates
                #     *
                #     *  P = 192.25       RA of galactic north pole (mean B1950.0)
                #     *  Q =  62.6        inclination of galactic to mean B1950.0 equator
                #     *  R =  33          longitude of ascending node
                #     *
                #     *  P,Q,R are degrees
                #     *
                #     *  Equatorial to galactic rotation matrix (J2000.0), obtained by
                #     *  applying the standard FK4 to FK5 transformation, for zero proper
                #     *  motion in FK5, to the columns of the B1950 equatorial to
                #     *  galactic rotation matrix:
                #     *
                #     
                #   Spherical to Cartesian
                #   Galactic to equatorial        
                #   Cartesian to spherical
                #   Express in conventional ranges
                # 
                #        * *+
                # *     - - - - - -
                # *      D I M X V
                # *     - - - - - -
                # *
                # *  Performs the 3-D backward unitary transformation:
                # *
                # *     vector VB = (inverse of matrix DM) * vector VA
                # *
                # *  (double precision)
                # *
                # *  (n.b.  the matrix must be unitary, as this routine assumes that
                # *   the inverse and transpose are identical)
                # *
                # *  Given:
                # *     DM       dp(3,3)    matrix
                # *     VA       dp(3)      vector
                # *
                # *  Returned:
                # *     VB       dp(3)      result vector
                # * 
                # 
                W = W + DM[I][J] * VA[I]
                I += 1
            VW[J] = W
            J += 1
        #  Static SLALIB like routines using Eclipse
        # 
        #          * *+
        # *     - - - - -
        # *      D S E P
        # *     - - - - -
        # *
        # *  Angle between two points on a sphere.
        # *
        # *  (double precision)
        # *
        # *  Given:
        # *     A1,B1    d     spherical coordinates of one point
        # *     A2,B2    d     spherical coordinates of the other point
        # *
        # *  (The spherical coordinates are [RA,Dec], [Long,Lat] etc, in radians.)
        # *
        # *  The result is the angle, in radians, between the two points.  It
        # *  is always positive.
        # *
        # *  Called:  sla_DCS2C, sla_DSEPV
        # *
        # *  Last revision:   7 May 2000
        # *
        # *  Copyright P.T.Wallace.  All rights reserved.
        # *
        # *-
        #          
        #   Convert coordinates from spherical to Cartesian.
        # 
        #          * *+
        # *     - - - - - -
        # *      D S E P V
        # *     - - - - - -
        # *
        # *  Angle between two vectors.
        # *
        # *  (double precision)
        # *
        # *  Given:
        # *     V1      d(3)    first vector
        # *     V2      d(3)    second vector
        # *
        # *  The result is the angle, in radians, between the two vectors.  It
        # *  is always positive.
        # *
        # *  Notes:
        # *
        # *  1  There is no requirement for the vectors to be unit length.
        # *
        # *  2  If either vector is null, zero is returned.
        # *
        # *  3  The simplest formulation would use dot product alone.  However,
        # *     this would reduce the accuracy for angles near zero and pi.  The
        # *     algorithm uses both cross product and dot product, which maintains
        # *     accuracy for all sizes of angle.
        # *
        # *  Called:  sla_DVXV, sla_DVN, sla_DVDV
        #          
        # sla_DVDV;
        #  Wv[3] is S
        # *  Dot product = cosine multiplied by the two moduli.
        # *  Angle between the vectors.
        # 
        #          * *+
        # *     - - - - -
        # *      D V D V
        # *     - - - - -
        # *
        # *  Scalar product of two 3-vectors  (double precision)
        # *
        # *  Given:
        # *      VA      dp(3)     first vector
        # *      VB      dp(3)     second vector
        # *
        # *  The result is the scalar product VA.VB (double precision)
        # *
        # *  P.T.Wallace   Starlink   November 1984
        # *
        # *  Copyright (C) 1995 Rutherford Appleton Laboratory
        # *
        #          
        # 
        #          * *+
        # *     - - - -
        # *      D V N
        # *     - - - -
        # *
        # *  Normalizes a 3-vector also giving the modulus (double precision)
        # *
        # *  Given:
        # *     V       dp(3)      vector
        # *
        # *  Returned:
        # *     UV      dp(3)      unit vector in direction of V
        # *     VM      dp         modulus of V
        # *
        # *  If the modulus of V is zero, UV is set to zero as well
        # *
        # *  P.T.Wallace   Starlink   23 November 1995
        #          
        #  4 not 3 
        # *  Modulus
        #  *  Normalize the vector
        # 
        #          * *+
        # *     - - - - -
        # *      D V X V
        # *     - - - - -
        # *
        # *  Vector product of two 3-vectors  (double precision)
        # *
        # *  Given:
        # *      VA      dp(3)     first vector
        # *      VB      dp(3)     second vector
        # *
        # *  Returned:
        # *      VC      dp(3)     vector result
        # *
        # *  P.T.Wallace   Starlink   March 1986
        # *
        # *  Copyright (C) 1995 Rutherford Appleton Laboratory
        # *
        #          
        # *  Form the vector product VA cross VB
        # *  Return the result
        # 
        #  * * Spherical coordinates to direction cosines (double precision)
        #  * 
        #  * Given: A,B dp spherical coordinates in radians (RA,Dec), (Long,Lat) etc
        #  * 
        #  * Returned: V dp(3) x,y,z unit vector
        #  * 
        #  * The spherical coordinates are longitude (+ve anticlockwise looking from the
        #  * +ve latitude pole) and latitude. The Cartesian coordinates are right handed,
        #  * with the x axis at zero longitude and latitude, and the z axis at the +ve
        #  * latitude pole.
        #  *  
        #  
        # 
        #  * * Direction cosines to spherical coordinates (double precision)
        #  * 
        #  * Given: V d(3) x,y,z vector
        #  * 
        #  * Returned: A,B d spherical coordinates in radians
        #  * 
        #  * The spherical coordinates are longitude (+ve anticlockwise looking from the
        #  * +ve latitude pole) and latitude. The Cartesian coordinates are right handed,
        #  * with the x axis at zero longitude and latitude, and the z axis at the +ve
        #  * latitude pole.
        #  * 
        #  * If V is null, zero A and B are returned. At either pole, zero A is returned.
        #  *  
        #  
        # 
        #  * * Convert B1950.0 FK4 star data to J2000.0 FK5 assuming zero proper motion in
        #  * the FK5 frame (double precision)
        #  * 
        #  * This routine converts stars from the old, Bessel-Newcomb, FK4 system to the
        #  * new, IAU 1976, FK5, Fricke system, in such a way that the FK5 proper motion
        #  * is zero. Because such a star has, in general, a non-zero proper motion in the
        #  * FK4 system, the routine requires the epoch at which the position in the FK4
        #  * system was determined.
        #  * 
        #  * The method is from Appendix 2 of Ref 1, but using the constants of Ref 4.
        #  * 
        #  * Given: R1950,D1950 dp B1950.0 FK4 RA,Dec at epoch (rad) BEPOCH dp Besselian
        #  * epoch (e.g. 1979.3D0)
        #  * 
        #  * Returned: R2000,D2000 dp J2000.0 FK5 RA,Dec (rad)
        #  * 
        #  * Notes:
        #  * 
        #  * 1) The epoch BEPOCH is strictly speaking Besselian, but if a Julian epoch is
        #  * supplied the result will be affected only to a negligible extent.
        #  * 
        #  * 2) Conversion from Besselian epoch 1950.0 to Julian epoch 2000.0 only is
        #  * provided for. Conversions involving other epochs will require use of the
        #  * appropriate precession, proper motion, and E-terms routines before and/or
        #  * after FK45Z is called.
        #  * 
        #  * 3) In the FK4 catalogue the proper motions of stars within 10 degrees of the
        #  * poles do not embody the differential E-term effect and should, strictly
        #  * speaking, be handled in a different manner from stars outside these regions.
        #  * However, given the general lack of homogeneity of the star data available for
        #  * routine astrometry, the difficulties of handling positions that may have been
        #  * determined from astrometric fields spanning the polar and non-polar regions,
        #  * the likelihood that the differential E-terms effect was not taken into
        #  * account when allowing for proper motion in past astrometry, and the
        #  * undesirability of a discontinuity in the algorithm, the decision has been
        #  * made in this routine to include the effect of differential E-terms on the
        #  * proper motions for all stars, whether polar or not. At epoch 2000, and
        #  * measuring on the sky rather than in terms of dRA, the errors resulting from
        #  * this simplification are less than 1 milliarcsecond in position and 1
        #  * milliarcsecond per century in proper motion.
        #  * 
        #  * References:
        #  * 
        #  * 1 Aoki,S., et al, 1983. Astron.Astrophys., 128, 263.
        #  * 
        #  * 2 Smith, C.A. et al, 1989. "The transformation of astrometric catalog systems
        #  * to the equinox J2000.0". Astron.J. 97, 265.
        #  * 
        #  * 3 Yallop, B.D. et al, 1989. "Transformation of mean star places from FK4
        #  * B1950.0 to FK5 J2000.0 using matrices in 6-space". Astron.J. 97, 274.
        #  * 
        #  * 4 Seidelmann, P.K. (ed), 1992. "Explanatory Supplement to the Astronomical
        #  * Almanac", ISBN 0-935702-68-7.
        #  * 
        #  * Called: sla_DCS2C, sla_EPJ, sla_EPB2D, sla_DCC2S, sla_DRANRM
        #  
        # 
        #  * * Transformation from J2000.0 equatorial coordinates to IAU 1958 galactic
        #  * coordinates (double precision)
        #  * 
        #  * Given: DR,DD dp J2000.0 RA,Dec
        #  * 
        #  * Returned: DL,DB dp galactic longitude and latitude L2,B2
        #  * 
        #  * (all arguments are radians)
        #  * 
        #  * Called: sla_DCS2C, sla_DMXV, sla_DCC2S, sla_DRANRM, sla_DRANGE
        #  * 
        #  * Note: The equatorial coordinates are J2000.0. Use the routine sla_EG50 if
        #  * conversion from B1950.0 'FK4' coordinates is required.
        #  
        # 
        #  * * Performs the 3-D forward unitary transformation:
        #  * 
        #  * vector VB = matrix DM * vector VA
        #  * 
        #  * (double precision)
        #  * 
        #  * Given: DM dp(3,3) matrix VA dp(3) vector
        #  * 
        #  * Returned: VB dp(3) result vector
        #  *  
        #  
        # 
        #  * * Projection of spherical coordinates onto tangent plane: "gnomonic"
        #  * projection - "standard coordinates" (double precision)
        #  * 
        #  * Given: RA,DEC dp spherical coordinates of point to be projected RAZ,DECZ dp
        #  * spherical coordinates of tangent point
        #  * 
        #  * Returned: XI,ETA dp rectangular coordinates on tangent plane J int status: 0 =
        #  * OK, star on tangent plane 1 = error, star too far from axis 2 = error,
        #  * antistar on tangent plane 3 = error, antistar too far from axis
        #  *  
        #  
        # XI
        # ETA
        # 
        #      * + - - - - - - D T P 2 S - - - - - -
        #      * 
        #      * Transform tangent plane coordinates into spherical (double precision)
        #      * 
        #      * Given: XI,ETA dp tangent plane rectangular coordinates RAZ,DECZ dp
        #      * spherical coordinates of tangent point
        #      * 
        #      * Returned: RA,DEC dp spherical coordinates (0-2pi,+/-pi/2)
        #      * 
        #      * Called: sla_DRANRM
        #      * 
        #      * P.T.Wallace Starlink 24 July 1995
        #      *  
        #      
        # 
        #  * * Conversion of Besselian Epoch to Modified Julian Date (double precision)
        #  * 
        #  * Given: EPB dp Besselian Epoch
        #  * 
        #  * The result is the Modified Julian Date (JD - 2400000.5).
        #  
        # 
        #  * * Conversion of Modified Julian Date to Julian Epoch (double precision)
        #  * 
        #  * Given: DATE dp Modified Julian Date (JD - 2400000.5)
        #  * 
        #  * The result is the Julian Epoch.
        #  
        # 
        #  * * Normalize angle into range 0-2 pi (double precision)
        #  * 
        #  * Given: ANGLE dp the angle in radians
        #  * 
        #  * The result is ANGLE expressed in the range 0-2 pi (double precision).
        #  
        # 
        #  * * Normalize angle into range +/- pi (double precision)
        #  * 
        #  * Given: ANGLE dp the angle in radians
        #  * 
        #  * The result (double precision) is ANGLE expressed in the range +/- pi.
        #  
        # 
        #     *+
        #     *     - - - - - -
        #     *      G A L E Q
        #     *     - - - - - -
        #     *
        #     *  Transformation from IAU 1958 galactic coordinates to
        #     *  J2000.0 equatorial coordinates (double precision)
        #     *
        #     *  Given:
        #     *     DL,DB       dp       galactic longitude and latitude L2,B2
        #     *
        #     *  Returned:
        #     *     DR,DD       dp       J2000.0 RA,Dec
        #     *
        #     *  (all arguments are radians)
        #     *
        #     *  Called:
        #     *     sla_DCS2C, sla_DIMXV, sla_DCC2S, sla_DRANRM, sla_DRANGE
        #     *
        #     *  Note:
        #     *     The equatorial coordinates are J2000.0.  Use the routine
        #     *     sla_GE50 if conversion to B1950.0 'FK4' coordinates is
        #     *     required.
        #     *
        #     *  Reference:
        #     *     Blaauw et al, Mon.Not.R.Astron.Soc.,121,123 (1960)
        #     *
        #     *  P.T.Wallace   Starlink   21 September 1998
        #     *
        #     *  Copyright (C) 1998 Rutherford Appleton Laboratory
        #     *
        #     *
        #     *  L2,B2 system of galactic coordinates
        #     *
        #     *  P = 192.25       RA of galactic north pole (mean B1950.0)
        #     *  Q =  62.6        inclination of galactic to mean B1950.0 equator
        #     *  R =  33          longitude of ascending node
        #     *
        #     *  P,Q,R are degrees
        #     *
        #     *  Equatorial to galactic rotation matrix (J2000.0), obtained by
        #     *  applying the standard FK4 to FK5 transformation, for zero proper
        #     *  motion in FK5, to the columns of the B1950 equatorial to
        #     *  galactic rotation matrix:
        #     *
        #     
        #   Spherical to Cartesian
        #   Galactic to equatorial        
        #   Cartesian to spherical
        #   Express in conventional ranges
        # 
        #        * *+
        # *     - - - - - -
        # *      D I M X V
        # *     - - - - - -
        # *
        # *  Performs the 3-D backward unitary transformation:
        # *
        # *     vector VB = (inverse of matrix DM) * vector VA
        # *
        # *  (double precision)
        # *
        # *  (n.b.  the matrix must be unitary, as this routine assumes that
        # *   the inverse and transpose are identical)
        # *
        # *  Given:
        # *     DM       dp(3,3)    matrix
        # *     VA       dp(3)      vector
        # *
        # *  Returned:
        # *     VB       dp(3)      result vector
        # * 
        # 
        J = 0
        while J < 3:
            #  Static SLALIB like routines using Eclipse
            # 
            #          * *+
            # *     - - - - -
            # *      D S E P
            # *     - - - - -
            # *
            # *  Angle between two points on a sphere.
            # *
            # *  (double precision)
            # *
            # *  Given:
            # *     A1,B1    d     spherical coordinates of one point
            # *     A2,B2    d     spherical coordinates of the other point
            # *
            # *  (The spherical coordinates are [RA,Dec], [Long,Lat] etc, in radians.)
            # *
            # *  The result is the angle, in radians, between the two points.  It
            # *  is always positive.
            # *
            # *  Called:  sla_DCS2C, sla_DSEPV
            # *
            # *  Last revision:   7 May 2000
            # *
            # *  Copyright P.T.Wallace.  All rights reserved.
            # *
            # *-
            #          
            #   Convert coordinates from spherical to Cartesian.
            # 
            #          * *+
            # *     - - - - - -
            # *      D S E P V
            # *     - - - - - -
            # *
            # *  Angle between two vectors.
            # *
            # *  (double precision)
            # *
            # *  Given:
            # *     V1      d(3)    first vector
            # *     V2      d(3)    second vector
            # *
            # *  The result is the angle, in radians, between the two vectors.  It
            # *  is always positive.
            # *
            # *  Notes:
            # *
            # *  1  There is no requirement for the vectors to be unit length.
            # *
            # *  2  If either vector is null, zero is returned.
            # *
            # *  3  The simplest formulation would use dot product alone.  However,
            # *     this would reduce the accuracy for angles near zero and pi.  The
            # *     algorithm uses both cross product and dot product, which maintains
            # *     accuracy for all sizes of angle.
            # *
            # *  Called:  sla_DVXV, sla_DVN, sla_DVDV
            #          
            # sla_DVDV;
            #  Wv[3] is S
            # *  Dot product = cosine multiplied by the two moduli.
            # *  Angle between the vectors.
            # 
            #          * *+
            # *     - - - - -
            # *      D V D V
            # *     - - - - -
            # *
            # *  Scalar product of two 3-vectors  (double precision)
            # *
            # *  Given:
            # *      VA      dp(3)     first vector
            # *      VB      dp(3)     second vector
            # *
            # *  The result is the scalar product VA.VB (double precision)
            # *
            # *  P.T.Wallace   Starlink   November 1984
            # *
            # *  Copyright (C) 1995 Rutherford Appleton Laboratory
            # *
            #          
            # 
            #          * *+
            # *     - - - -
            # *      D V N
            # *     - - - -
            # *
            # *  Normalizes a 3-vector also giving the modulus (double precision)
            # *
            # *  Given:
            # *     V       dp(3)      vector
            # *
            # *  Returned:
            # *     UV      dp(3)      unit vector in direction of V
            # *     VM      dp         modulus of V
            # *
            # *  If the modulus of V is zero, UV is set to zero as well
            # *
            # *  P.T.Wallace   Starlink   23 November 1995
            #          
            #  4 not 3 
            # *  Modulus
            #  *  Normalize the vector
            # 
            #          * *+
            # *     - - - - -
            # *      D V X V
            # *     - - - - -
            # *
            # *  Vector product of two 3-vectors  (double precision)
            # *
            # *  Given:
            # *      VA      dp(3)     first vector
            # *      VB      dp(3)     second vector
            # *
            # *  Returned:
            # *      VC      dp(3)     vector result
            # *
            # *  P.T.Wallace   Starlink   March 1986
            # *
            # *  Copyright (C) 1995 Rutherford Appleton Laboratory
            # *
            #          
            # *  Form the vector product VA cross VB
            # *  Return the result
            # 
            #  * * Spherical coordinates to direction cosines (double precision)
            #  * 
            #  * Given: A,B dp spherical coordinates in radians (RA,Dec), (Long,Lat) etc
            #  * 
            #  * Returned: V dp(3) x,y,z unit vector
            #  * 
            #  * The spherical coordinates are longitude (+ve anticlockwise looking from the
            #  * +ve latitude pole) and latitude. The Cartesian coordinates are right handed,
            #  * with the x axis at zero longitude and latitude, and the z axis at the +ve
            #  * latitude pole.
            #  *  
            #  
            # 
            #  * * Direction cosines to spherical coordinates (double precision)
            #  * 
            #  * Given: V d(3) x,y,z vector
            #  * 
            #  * Returned: A,B d spherical coordinates in radians
            #  * 
            #  * The spherical coordinates are longitude (+ve anticlockwise looking from the
            #  * +ve latitude pole) and latitude. The Cartesian coordinates are right handed,
            #  * with the x axis at zero longitude and latitude, and the z axis at the +ve
            #  * latitude pole.
            #  * 
            #  * If V is null, zero A and B are returned. At either pole, zero A is returned.
            #  *  
            #  
            # 
            #  * * Convert B1950.0 FK4 star data to J2000.0 FK5 assuming zero proper motion in
            #  * the FK5 frame (double precision)
            #  * 
            #  * This routine converts stars from the old, Bessel-Newcomb, FK4 system to the
            #  * new, IAU 1976, FK5, Fricke system, in such a way that the FK5 proper motion
            #  * is zero. Because such a star has, in general, a non-zero proper motion in the
            #  * FK4 system, the routine requires the epoch at which the position in the FK4
            #  * system was determined.
            #  * 
            #  * The method is from Appendix 2 of Ref 1, but using the constants of Ref 4.
            #  * 
            #  * Given: R1950,D1950 dp B1950.0 FK4 RA,Dec at epoch (rad) BEPOCH dp Besselian
            #  * epoch (e.g. 1979.3D0)
            #  * 
            #  * Returned: R2000,D2000 dp J2000.0 FK5 RA,Dec (rad)
            #  * 
            #  * Notes:
            #  * 
            #  * 1) The epoch BEPOCH is strictly speaking Besselian, but if a Julian epoch is
            #  * supplied the result will be affected only to a negligible extent.
            #  * 
            #  * 2) Conversion from Besselian epoch 1950.0 to Julian epoch 2000.0 only is
            #  * provided for. Conversions involving other epochs will require use of the
            #  * appropriate precession, proper motion, and E-terms routines before and/or
            #  * after FK45Z is called.
            #  * 
            #  * 3) In the FK4 catalogue the proper motions of stars within 10 degrees of the
            #  * poles do not embody the differential E-term effect and should, strictly
            #  * speaking, be handled in a different manner from stars outside these regions.
            #  * However, given the general lack of homogeneity of the star data available for
            #  * routine astrometry, the difficulties of handling positions that may have been
            #  * determined from astrometric fields spanning the polar and non-polar regions,
            #  * the likelihood that the differential E-terms effect was not taken into
            #  * account when allowing for proper motion in past astrometry, and the
            #  * undesirability of a discontinuity in the algorithm, the decision has been
            #  * made in this routine to include the effect of differential E-terms on the
            #  * proper motions for all stars, whether polar or not. At epoch 2000, and
            #  * measuring on the sky rather than in terms of dRA, the errors resulting from
            #  * this simplification are less than 1 milliarcsecond in position and 1
            #  * milliarcsecond per century in proper motion.
            #  * 
            #  * References:
            #  * 
            #  * 1 Aoki,S., et al, 1983. Astron.Astrophys., 128, 263.
            #  * 
            #  * 2 Smith, C.A. et al, 1989. "The transformation of astrometric catalog systems
            #  * to the equinox J2000.0". Astron.J. 97, 265.
            #  * 
            #  * 3 Yallop, B.D. et al, 1989. "Transformation of mean star places from FK4
            #  * B1950.0 to FK5 J2000.0 using matrices in 6-space". Astron.J. 97, 274.
            #  * 
            #  * 4 Seidelmann, P.K. (ed), 1992. "Explanatory Supplement to the Astronomical
            #  * Almanac", ISBN 0-935702-68-7.
            #  * 
            #  * Called: sla_DCS2C, sla_EPJ, sla_EPB2D, sla_DCC2S, sla_DRANRM
            #  
            # 
            #  * * Transformation from J2000.0 equatorial coordinates to IAU 1958 galactic
            #  * coordinates (double precision)
            #  * 
            #  * Given: DR,DD dp J2000.0 RA,Dec
            #  * 
            #  * Returned: DL,DB dp galactic longitude and latitude L2,B2
            #  * 
            #  * (all arguments are radians)
            #  * 
            #  * Called: sla_DCS2C, sla_DMXV, sla_DCC2S, sla_DRANRM, sla_DRANGE
            #  * 
            #  * Note: The equatorial coordinates are J2000.0. Use the routine sla_EG50 if
            #  * conversion from B1950.0 'FK4' coordinates is required.
            #  
            # 
            #  * * Performs the 3-D forward unitary transformation:
            #  * 
            #  * vector VB = matrix DM * vector VA
            #  * 
            #  * (double precision)
            #  * 
            #  * Given: DM dp(3,3) matrix VA dp(3) vector
            #  * 
            #  * Returned: VB dp(3) result vector
            #  *  
            #  
            # 
            #  * * Projection of spherical coordinates onto tangent plane: "gnomonic"
            #  * projection - "standard coordinates" (double precision)
            #  * 
            #  * Given: RA,DEC dp spherical coordinates of point to be projected RAZ,DECZ dp
            #  * spherical coordinates of tangent point
            #  * 
            #  * Returned: XI,ETA dp rectangular coordinates on tangent plane J int status: 0 =
            #  * OK, star on tangent plane 1 = error, star too far from axis 2 = error,
            #  * antistar on tangent plane 3 = error, antistar too far from axis
            #  *  
            #  
            # XI
            # ETA
            # 
            #      * + - - - - - - D T P 2 S - - - - - -
            #      * 
            #      * Transform tangent plane coordinates into spherical (double precision)
            #      * 
            #      * Given: XI,ETA dp tangent plane rectangular coordinates RAZ,DECZ dp
            #      * spherical coordinates of tangent point
            #      * 
            #      * Returned: RA,DEC dp spherical coordinates (0-2pi,+/-pi/2)
            #      * 
            #      * Called: sla_DRANRM
            #      * 
            #      * P.T.Wallace Starlink 24 July 1995
            #      *  
            #      
            # 
            #  * * Conversion of Besselian Epoch to Modified Julian Date (double precision)
            #  * 
            #  * Given: EPB dp Besselian Epoch
            #  * 
            #  * The result is the Modified Julian Date (JD - 2400000.5).
            #  
            # 
            #  * * Conversion of Modified Julian Date to Julian Epoch (double precision)
            #  * 
            #  * Given: DATE dp Modified Julian Date (JD - 2400000.5)
            #  * 
            #  * The result is the Julian Epoch.
            #  
            # 
            #  * * Normalize angle into range 0-2 pi (double precision)
            #  * 
            #  * Given: ANGLE dp the angle in radians
            #  * 
            #  * The result is ANGLE expressed in the range 0-2 pi (double precision).
            #  
            # 
            #  * * Normalize angle into range +/- pi (double precision)
            #  * 
            #  * Given: ANGLE dp the angle in radians
            #  * 
            #  * The result (double precision) is ANGLE expressed in the range +/- pi.
            #  
            # 
            #     *+
            #     *     - - - - - -
            #     *      G A L E Q
            #     *     - - - - - -
            #     *
            #     *  Transformation from IAU 1958 galactic coordinates to
            #     *  J2000.0 equatorial coordinates (double precision)
            #     *
            #     *  Given:
            #     *     DL,DB       dp       galactic longitude and latitude L2,B2
            #     *
            #     *  Returned:
            #     *     DR,DD       dp       J2000.0 RA,Dec
            #     *
            #     *  (all arguments are radians)
            #     *
            #     *  Called:
            #     *     sla_DCS2C, sla_DIMXV, sla_DCC2S, sla_DRANRM, sla_DRANGE
            #     *
            #     *  Note:
            #     *     The equatorial coordinates are J2000.0.  Use the routine
            #     *     sla_GE50 if conversion to B1950.0 'FK4' coordinates is
            #     *     required.
            #     *
            #     *  Reference:
            #     *     Blaauw et al, Mon.Not.R.Astron.Soc.,121,123 (1960)
            #     *
            #     *  P.T.Wallace   Starlink   21 September 1998
            #     *
            #     *  Copyright (C) 1998 Rutherford Appleton Laboratory
            #     *
            #     *
            #     *  L2,B2 system of galactic coordinates
            #     *
            #     *  P = 192.25       RA of galactic north pole (mean B1950.0)
            #     *  Q =  62.6        inclination of galactic to mean B1950.0 equator
            #     *  R =  33          longitude of ascending node
            #     *
            #     *  P,Q,R are degrees
            #     *
            #     *  Equatorial to galactic rotation matrix (J2000.0), obtained by
            #     *  applying the standard FK4 to FK5 transformation, for zero proper
            #     *  motion in FK5, to the columns of the B1950 equatorial to
            #     *  galactic rotation matrix:
            #     *
            #     
            #   Spherical to Cartesian
            #   Galactic to equatorial        
            #   Cartesian to spherical
            #   Express in conventional ranges
            # 
            #        * *+
            # *     - - - - - -
            # *      D I M X V
            # *     - - - - - -
            # *
            # *  Performs the 3-D backward unitary transformation:
            # *
            # *     vector VB = (inverse of matrix DM) * vector VA
            # *
            # *  (double precision)
            # *
            # *  (n.b.  the matrix must be unitary, as this routine assumes that
            # *   the inverse and transpose are identical)
            # *
            # *  Given:
            # *     DM       dp(3,3)    matrix
            # *     VA       dp(3)      vector
            # *
            # *  Returned:
            # *     VB       dp(3)      result vector
            # * 
            # 
            VB[J] = VW[J]
            J += 1
        return VB
