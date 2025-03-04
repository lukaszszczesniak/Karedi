package com.github.nianna.karedi.song.tag;

import com.github.nianna.karedi.util.Language;

import java.util.Arrays;
import java.util.List;

class TagKeySuggestedValues {

    private TagKeySuggestedValues() {

    }

    static List<?> forKey(TagKey tagKey) {
        return switch (tagKey) {
            case VERSION -> Arrays.asList(FormatSpecification.values());
            case LANGUAGE -> Arrays.stream(Language.values())
                    .filter(lang -> lang != Language.ESPANOL)
                    .toList();
            case CALCMEDLEY -> List.of("on", "off");
            case GENRE -> List.of("Alternative", "Alternative Rock", "Ballad", "Blues", "C-Pop", "Country",
                    "Dance", "Disco", "Electronic", "Folk", "Hard Rock", "Heavy Metal", "Hip-Hop", "Indie", "J-Pop",
                    "J-Rock", "Jazz", "K-Pop", "Metal", "New Wave", "Oldies", "Pop", "Pop Rock", "Power Metal",
                    "Progressive Metal", "Progressive Rock", "Punk", "Punk Rock", "R&B", "Reggae", "Rock", "Schlager",
                    "Soft Rock", "Soul", "Techno", "Trance");
            case TAGS -> List.of("Anime", "Charts", "Christmas", "Club", "Cover", "Disney", "Duet",
                    "Eurovision Song Contest", "Explicit", "Fan Song", "Feel-Good", "Funny", "Guilty Pleasure",
                    "Halloween", "Heartbreak", "Live", "Love Song", "Mainstream", "Movies", "Musical", "Party",
                    "Pride/LGBTQ", "Relaxed", "Slow", "Song-checked", "Special interest", "Summer", "TV Show",
                    "Underground", "Underrated", "Video Game", "Viral Hit", "Vocaloid");
            default -> List.of();
        };
    }
}
