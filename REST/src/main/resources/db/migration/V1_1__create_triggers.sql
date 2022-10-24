CREATE OR REPLACE FUNCTION delete_operations_from_account() RETURNS TRIGGER AS $$
BEGIN
    DELETE FROM operation WHERE account=OLD.id;
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER account_deletion BEFORE DELETE
    ON account
FOR EACH ROW EXECUTE FUNCTION delete_operations_from_account();


CREATE OR REPLACE  FUNCTION delete_accounts_from_user() RETURNS TRIGGER AS $$
BEGIN
    DELETE FROM account WHERE holder=OLD.id;
    DELETE FROM operation_type WHERE creator=OLD.id;
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER user_deletion BEFORE DELETE
    ON "user"
FOR EACH ROW EXECUTE FUNCTION delete_accounts_from_user();


CREATE OR REPLACE  FUNCTION delete_operations_by_operation_type() RETURNS TRIGGER AS $$
BEGIN
    DELETE FROM operation WHERE type=OLD.id;
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER operation_type_deletion BEFORE DELETE
    ON operation_type
FOR EACH ROW EXECUTE FUNCTION delete_operations_by_operation_type();


CREATE OR REPLACE  FUNCTION update_balance() RETURNS TRIGGER AS $$
BEGIN
    CASE
        WHEN tg_argv[0] = 'delete'
            THEN
                UPDATE account SET balance=balance + (-1)*OLD.sum WHERE id=OLD.account; /* undo operation */
                RETURN OLD;
        WHEN tg_argv[0] = 'insert'
            THEN
                UPDATE account SET balance=balance + NEW.sum WHERE id=NEW.account; /* apply operation */
                RETURN NEW;
        WHEN tg_argv[0] = 'update'
            THEN
                UPDATE account SET balance=balance + (-1)*OLD.sum WHERE id=OLD.account; /* undo operation */
                UPDATE account SET balance=balance + NEW.sum WHERE id=NEW.account; /* apply operation */
                RETURN NEW;
    END CASE;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER operation_deletion BEFORE DELETE
    ON operation
FOR EACH ROW EXECUTE FUNCTION update_balance('delete');

CREATE TRIGGER operation_insertion BEFORE INSERT
    ON operation
FOR EACH ROW EXECUTE FUNCTION update_balance('insert');

CREATE TRIGGER operation_update BEFORE UPDATE
    ON operation
FOR EACH ROW EXECUTE FUNCTION update_balance('update');


/*
// TESTS


// Code to test account_deletion trigger
INSERT INTO "user"(name, email, password) VALUES('fwefew', 'bkerjlk@fjewok.com', 'fjkewljfkjwefkewjflkwe');
SELECT * FROM "user";

INSERT INTO account(holder, title) VALUES('', 'some title');
SELECT * FROM account;

INSERT INTO operation(account, sum) VALUES('', 10);
SELECT * FROM operation;

DELETE FROM account WHERE id='';

SELECT * FROM operation;
   // MUST BE EMPTY


// TODO Test
// Code to test user_deletion trigger
INSERT INTO "user"(name, email, password) VALUES('fwefew', 'bkerjlk@fjewok.com', 'fjkewljfkjwefkewjflkwe');
SELECT * FROM "user";

INSERT INTO account(holder, title) VALUES('', 'some title');
SELECT * FROM account;

INSERT INTO operation(account, sum) VALUES('', 10);
SELECT * FROM operation;

DELETE FROM "user" WHERE name='fwefew';

SELECT * FROM operation;

// TODO Write test on operation_type deletion

// Code to test operation deletion trigger
INSERT INTO "user"(name, email, password) VALUES('fwefew', 'bkerjlk@fjewok.com', 'fjkewljfkjwefkewjflkwe');
SELECT * FROM "user";

INSERT INTO account(holder, title) VALUES('', 'some title');
SELECT * FROM account;

INSERT INTO operation(account, sum) VALUES('', 10);
SELECT * FROM operation;

SELECT * FROM account WHERE id='';
// BALANCE MUST BE 10

DELETE FROM operation;

SELECT * FROM account WHERE id='';
// BALANCE MUST BE 0

 */
