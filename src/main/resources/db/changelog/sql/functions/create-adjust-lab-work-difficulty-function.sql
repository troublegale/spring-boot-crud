CREATE OR REPLACE FUNCTION adjust_lab_work_difficulty(lab_id BIGINT, steps INTEGER, diff_vals VARCHAR[])
    RETURNS VARCHAR AS
'
DECLARE
    len   INTEGER := CARDINALITY(diff_vals);
    diff  VARCHAR;
    index INTEGER;
BEGIN
    SELECT difficulty
    INTO diff
    FROM lab_works
    WHERE id = lab_id;

    FOR i IN 1..len
        LOOP
            index = i - 1;
            EXIT WHEN diff = diff_vals[i];
        END LOOP;

    index = ABS(MOD(index + steps + len, len));
    diff = diff_vals[index + 1];

    IF diff IS NULL THEN
        RETURN index;
    END IF;

    UPDATE lab_works
    SET difficulty = diff
    WHERE id = lab_id;

    RETURN diff;

END;
' LANGUAGE plpgsql;

