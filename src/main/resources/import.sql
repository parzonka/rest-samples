/** timezone **/

INSERT INTO timezone (timezoneId, timezoneName, version) values (-1, 'Europe/Berlin', 0);

/** vessel **/

INSERT INTO translation (translationId, version) values (-1, 0);

INSERT INTO text (translationId, textLanguage, textString) values (-1, 'en', 'Klingon Warbird');
INSERT INTO text (translationId, textLanguage, textString) values (-1, 'de', 'Klingonischer Jäger');

INSERT INTO vessel(vesselId, vesselName, version) values (-1, -1, 0);



INSERT INTO translation (translationId, version) values (-2, 0);

INSERT INTO text (translationId, textLanguage, textString) values (-2, 'en', 'Space Shuttle');
INSERT INTO text (translationId, textLanguage, textString) values (-2, 'de', 'Space Shuttle');

INSERT INTO vessel(vesselId, vesselName, version) values (-2, -2, 0);



/** sighting **/

INSERT INTO sighting (sightingId, vesselId, sightingMemo, version) values (-1, -1, 'unheimlich!', 0)