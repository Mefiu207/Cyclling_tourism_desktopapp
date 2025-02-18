CREATE OR REPLACE FUNCTION update_pokoje_on_add()
    RETURNS TRIGGER AS $$
        BEGIN
            UPDATE pokoje
            SET il_klientow = il_klientow + 1
            WHERE id = NEW.pokoj;

            RETURN NEW;
        END;
    $$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION update_wycieczki_on_add()
    RETURNS TRIGGER AS $$
        BEGIN
            UPDATE wycieczki
            SET il_uczestnikow = il_uczestnikow + 1
            WHERE wycieczka = NEW.wycieczka;

            RETURN NEW;
        end;
    $$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION update_pokoje_on_delete()
    RETURNS TRIGGER AS $$
BEGIN
    UPDATE pokoje
    SET il_klientow = il_klientow - 1
    WHERE id = OLD.pokoj;

    RETURN OLD;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION update_wycieczki_on_delete()
    RETURNS TRIGGER AS $$
BEGIN
    UPDATE wycieczki
    SET il_uczestnikow = il_uczestnikow - 1
    WHERE wycieczka = OLD.wycieczka;

    RETURN OLD;
end;
$$ LANGUAGE plpgsql;


CREATE TRIGGER trigger_on_add_klinet_pokoje
    AFTER INSERT
    ON klienci
    FOR EACH ROW
EXECUTE FUNCTION update_pokoje_on_add();


CREATE TRIGGER trigger_on_add_klinet_wycieczki
    AFTER INSERT
    ON klienci
    FOR EACH ROW
EXECUTE FUNCTION update_wycieczki_on_add();


CREATE TRIGGER trigger_on_delete_klinet_pokoje
    AFTER DELETE
    ON klienci
    FOR EACH ROW
EXECUTE FUNCTION update_pokoje_on_delete();


CREATE TRIGGER trigger_on_delete_klinet_wycieczki
    AFTER DELETE
    ON klienci
    FOR EACH ROW
EXECUTE FUNCTION update_wycieczki_on_delete();


CREATE OR REPLACE FUNCTION update_ceny_on_add()
RETURNS TRIGGER AS $$
    BEGIN
        UPDATE wycieczki
        SET wplyw = wplyw + NEW.do_zaplaty
        WHERE wycieczka = NEW.wycieczka;

        RETURN NEW;
    end;
    $$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION update_ceny_on_delete()
    RETURNS TRIGGER AS $$
BEGIN
    UPDATE wycieczki
    SET wplyw = wplyw - OLD.do_zaplaty
    WHERE wycieczka = OLD.wycieczka;

    RETURN OLD;
end;
$$ LANGUAGE plpgsql;


CREATE TRIGGER on_add_klient_ceny AFTER INSERT
    ON klienci FOR EACH ROW
    EXECUTE FUNCTION update_ceny_on_add();


CREATE TRIGGER on_delete_klient_ceny AFTER DELETE
    ON klienci FOR EACH ROW
EXECUTE FUNCTION update_ceny_on_delete();
