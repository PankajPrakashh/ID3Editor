/*
 * Copyright (C) 2017 Pankaj Prakash
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.codeforwin.id3;

/**
 * Represents the type of the attached picture with an APIC frame.
 * 
 * @author Pankaj Prakash
 * @version 1.0
 */
public enum ApicType {
    OTHER(0),
    FILE_ICON(1),
    OTHER_FILE_ICON(2),
    COVER_FRONT(3),
    COVER_BACK(4),
    LEAFLET_PAGE(5),
    MEDIA(6),
    LEAD_ARTIST(7),
    ARTIST(8),
    CONDUCTOR(9),
    BAND(10),
    COMPOSER(11),
    LYRICISTS(12),
    RECORDING_LOCATION(13),
    DURING_RECORDING(14),
    DURING_PERFORMANCE(15),
    MOVIE_VIDEO_SCREEN_CAPTURE(16),
    BRIGHT_COLOURED_FISH(17),
    ILLUSTRATION(18),
    BAND_ARTIST_LOGO(19),
    PUBLISHER_STUDIO_LOGO(20);
    
    private final int apicType;

    ApicType(int apicType) {
        this.apicType = apicType;
    }
}
