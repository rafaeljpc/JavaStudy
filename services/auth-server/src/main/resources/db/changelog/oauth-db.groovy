package db.io;

databaseChangeLog {

    changeSet(id: 'create-oauth-tables', author: 'rafaeljpc') {

        createTable(tableName: 'oauth_access_token') {
            column(name: 'authentication_id', type: 'varchar(256)') {
                constraints(nullable: false, primaryKey: true)
            }
            column(name: 'token_id', type: 'varchar(256)')
            column(name: 'token', type: 'bytea')
            column(name: 'user_name', type: 'varchar(256)')
            column(name: 'client_id', type: 'varchar(256)')
            column(name: 'authentication', type: 'bytea')
            column(name: 'refresh_token', type: 'varchar(256)')
        }

        createTable(tableName: 'oauth_refresh_token') {
            column(name: 'token_id', type: 'varchar(256)')
            column(name: 'token', type: 'bytea')
            column(name: 'authentication', type: 'bytea')
        }
    }

    changeSet(id: 'client-app-tables', author: 'rafaeljpc') {
        createTable(tableName: 'oauth_client_details') {
            column(name: 'client_id', type: 'varchar(255)') { constraints(nullable: false, primaryKey: true) }
            column(name: 'resource_ids', type: 'varchar(255)') { constraints(nullable: false) }
            column(name: 'client_secret', type: 'varchar(255)') { constraints(nullable: false) }
            column(name: 'scope', type: 'varchar(255)') { constraints(nullable: false) }
            column(name: 'authorized_grant_types', type: 'varchar(255)') { constraints(nullable: false) }
            column(name: 'web_server_redirect_uri', type: 'varchar(255)') { constraints(nullable: false) }
            column(name: 'authorities', type: 'varchar(255)') { constraints(nullable: false) }
            column(name: 'access_token_validity', type: 'int(11)') { constraints(nullable: false) }
            column(name: 'refresh_token_validity', type: 'int(11)') { constraints(nullable: false) }
            column(name: 'additional_information', type: 'varchar(4096)') { constraints(nullable: false) }
            column(name: 'autoapprove', type: 'varchar(255)') { constraints(nullable: false) }
        }

        insert(tableName: 'oauth_client_details') {
            column(name: 'client_id', value: 'adminapp')
            column(name: 'resource_ids', value: 'mw/adminapp,ms/admin,ms/user')
            column(name: 'client_secret', value: '{bcrypt}$2a$10$EOs8VROb14e7ZnydvXECA.4LoIhPOoFHKvVF/iBZ/ker17Eocz4Vi')
            column(name: 'scope', value: 'role_admin,role_superadmin')
            column(name: 'authorized_grant_types', value: 'authorization_code,password,refresh_token,implicit')
            column(name: 'web_server_redirect_uri', value: '')
            column(name: 'authorities', value: '')
            column(name: 'access_token_validity', valueNumeric: 900)
            column(name: 'refresh_token_validity', valueNumeric: 3600)
            column(name: 'additional_information', value: '{}')
            column(name: 'autoapprove', value: '')
        }
    }

    changeSet(id: 'users-permission-tables', author: 'rafaeljpc') {
        // Permission
        createTable(tableName: 'permission') {
            column(name: 'id', type: 'bigint', autoIncrement: true) {
                constraints(nullable: false, primaryKey: true)
            }
            column(name: 'name', type: 'varchar(60)') {
                constraints(nullable: false, unique: true)
            }
            column(name: 'created_on', type: 'datetime', defaultValueComputed: 'CURRENT_TIMESTAMP') {
                constraints(nullable: false )
            }
            column(name: 'updated_on', type: 'datetime', defaultValueComputed: 'CURRENT_TIMESTAMP') {
                constraints(nullable: false)
            }
            column(name: 'version', type: 'bigint', defaultValueNumeric: 0) {
                constraints(nullable: false)
            }
        }

        insert(tableName: 'permission') {
            column(name: 'id', valueNumeric: 1)
            column(name: 'name', value: 'can_delete_user')
        }
        insert(tableName: 'permission') {
            column(name: 'id', valueNumeric: 2)
            column(name: 'name', value: 'can_create_user')
        }
        insert(tableName: 'permission') {
            column(name: 'id', valueNumeric: 3)
            column(name: 'name', value: 'can_update_user')
        }
        insert(tableName: 'permission') {
            column(name: 'id', valueNumeric: 4)
            column(name: 'name', value: 'can_read_user')
        }

        // Role
        createTable(tableName: 'role') {
            column(name: 'id', type: 'bigint', autoIncrement: true) {
                constraints(nullable: false, primaryKey: true)
            }
            column(name: 'name', type: 'varchar(60)') {
                constraints(nullable: false, unique: true)
            }
            column(name: 'created_on', type: 'datetime', defaultValueComputed: 'CURRENT_TIMESTAMP') {
                constraints(nullable: false)
            }
            column(name: 'updated_on', type: 'datetime', defaultValueComputed: 'CURRENT_TIMESTAMP') {
                constraints(nullable: false)
            }
            column(name: 'version', type: 'bigint', defaultValueNumeric: 0) {
                constraints(nullable: false)
            }
        }

        insert(tableName: 'role') {
            column(name: 'id', valueNumeric: 1)
            column(name: 'name', value: 'role_admin')
        }
        insert(tableName: 'role') {
            column(name: 'id', valueNumeric: 2)
            column(name: 'name', value: 'role_user')
        }

        // Permission_Role
        createTable(tableName: 'permission_role') {
            column(name: 'permission_id', type: 'bigint', autoIncrement: true) {
                constraints(nullable: false)
            }
            column(name: 'role_id', type: 'bigint', autoIncrement: true) {
                constraints(nullable: false)
            }
            column(name: 'created_on', type: 'datetime', defaultValueComputed: 'CURRENT_TIMESTAMP') {
                constraints(nullable: false)
            }
            column(name: 'updated_on', type: 'datetime', defaultValueComputed: 'CURRENT_TIMESTAMP') {
                constraints(nullable: false)
            }
            column(name: 'version', type: 'bigint', defaultValueNumeric: 0) {
                constraints(nullable: false)
            }
        }

        addPrimaryKey(
                constraintName: 'pk_permission_role',
                tableName: 'permission_role',
                columnNames: 'permission_id, role_id'
        )
        addForeignKeyConstraint(
                constraintName: 'permission_role_fk1',
                baseColumnNames: 'permission_id',
                baseTableName: 'permission_role',
                onDelete: 'CASCADE',
                onUpdate: 'CASCADE',
                referencedColumnNames: 'id',
                referencedTableName: 'permission'
        )
        addForeignKeyConstraint(
                constraintName: 'permission_role_fk2',
                baseColumnNames: 'role_id',
                baseTableName: 'permission_role',
                onDelete: 'CASCADE',
                onUpdate: 'CASCADE',
                referencedColumnNames: 'id',
                referencedTableName: 'role'
        )

        insert(tableName: 'permission_role') {
            column(name: 'permission_id', valueNumeric: 1)
            column(name: 'role_id', valueNumeric: 1)
        }
        insert(tableName: 'permission_role') {
            column(name: 'permission_id', valueNumeric: 2)
            column(name: 'role_id', valueNumeric: 1)
        }
        insert(tableName: 'permission_role') {
            column(name: 'permission_id', valueNumeric: 3)
            column(name: 'role_id', valueNumeric: 1)
        }
        insert(tableName: 'permission_role') {
            column(name: 'permission_id', valueNumeric: 4)
            column(name: 'role_id', valueNumeric: 1)
        }
        insert(tableName: 'permission_role') {
            column(name: 'permission_id', valueNumeric: 4)
            column(name: 'role_id', valueNumeric: 2)
        }

        // User
        createTable(tableName: 'user') {
            column(name: 'id', type: 'bigint', autoIncrement: true) { constraints(nullable: false, primaryKey: true, unique: true) }
            column(name: 'username', type: 'varchar(24)') { constraints(nullable: false, unique: true) }
            column(name: 'password', type: 'varchar(200)') { constraints(nullable: false) }
            column(name: 'email', type: 'varchar(255)') { constraints(nullable: false) }
            column(name: 'enabled', type: 'boolean') { constraints(nullable: false) }
            column(name: 'account_expired', type: 'boolean') { constraints(nullable: false) }
            column(name: 'credentials_expired', type: 'boolean') { constraints(nullable: false) }
            column(name: 'account_locked', type: 'boolean') { constraints(nullable: false) }
            column(name: 'created_on', type: 'datetime', defaultValueComputed: 'CURRENT_TIMESTAMP') {
                constraints(nullable: false)
            }
            column(name: 'updated_on', type: 'datetime', defaultValueComputed: 'CURRENT_TIMESTAMP') {
                constraints(nullable: false)
            }
            column(name: 'version', type: 'bigint', defaultValueNumeric: 0) {
                constraints(nullable: false)
            }
        }

        insert(tableName: 'user'){
            column(name: 'id', valueNumeric: 1)
            column(name: 'username', value: 'rafaeljpc')
            column(name: 'password', value: '{bcrypt}$2a$10$EOs8VROb14e7ZnydvXECA.4LoIhPOoFHKvVF/iBZ/ker17Eocz4Vi')
            column(name: 'email', value: 'rafaeljpc@gmail.com')
            column(name: 'enabled', valueBoolean: true)
            column(name: 'account_expired', valueBoolean: false)
            column(name: 'credentials_expired', valueBoolean: false)
            column(name: 'account_locked', valueBoolean: false)
        }

        insert(tableName: 'user'){
            column(name: 'id', valueNumeric: 2)
            column(name: 'username', value: 'user')
            column(name: 'password', value: '{bcrypt}$2a$10$EOs8VROb14e7ZnydvXECA.4LoIhPOoFHKvVF/iBZ/ker17Eocz4Vi')
            column(name: 'email', value: 'user@example.com')
            column(name: 'enabled', valueBoolean: true)
            column(name: 'account_expired', valueBoolean: false)
            column(name: 'credentials_expired', valueBoolean: false)
            column(name: 'account_locked', valueBoolean: false)
        }

        // Role_User
        createTable(tableName: 'role_user') {
            column(name: 'role_id', type: 'bigint') { constraints(nullable: false) }
            column(name: 'user_id', type: 'bigint') { constraints(nullable: false) }
            column(name: 'created_on', type: 'datetime', defaultValueComputed: 'CURRENT_TIMESTAMP') {
                constraints(nullable: false)
            }
            column(name: 'updated_on', type: 'datetime', defaultValueComputed: 'CURRENT_TIMESTAMP') {
                constraints(nullable: false)
            }
            column(name: 'version', type: 'bigint', defaultValueNumeric: 0) {
                constraints(nullable: false)
            }
        }

        addPrimaryKey(
                constraintName: 'pk_role_user',
                tableName: 'role_user',
                columnNames: 'user_id, role_id'
        )
        addForeignKeyConstraint(
                constraintName: 'role_user_fk1',
                baseColumnNames: 'user_id',
                baseTableName: 'role_user',
                onDelete: 'CASCADE',
                onUpdate: 'CASCADE',
                referencedColumnNames: 'id',
                referencedTableName: 'user'
        )
        addForeignKeyConstraint(
                constraintName: 'role_user_fk2',
                baseColumnNames: 'role_id',
                baseTableName: 'role_user',
                onDelete: 'CASCADE',
                onUpdate: 'CASCADE',
                referencedColumnNames: 'id',
                referencedTableName: 'role'
        )

    }
}