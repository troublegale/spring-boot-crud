CREATE OR REPLACE FUNCTION get_lab_works_with_description_containing(d VARCHAR, page INTEGER, page_size INTEGER)
    RETURNS SETOF lab_works AS
'
BEGIN
    RETURN QUERY
        SELECT *
        FROM lab_works
        WHERE description LIKE ''%'' || d || ''%''
        ORDER BY id
        OFFSET page * page_size LIMIT page_size;
END;
' LANGUAGE plpgsql;
