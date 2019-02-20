package db.changelog

databaseChangeLog {
  changeSet(id: '1550682763143-1', author: 'rafae (generated)') {
    createSequence(sequenceName: 'hibernate_sequence')
  }

  changeSet(id: '1550682763143-2', author: 'rafae (generated)') {
    createTable(tableName: 'house') {
      column(name: 'id', type: 'BIGINT', autoIncrement: true) {
        constraints(primaryKey: true, primaryKeyName: 'housePK')
      }
      column(name: 'name', type: 'VARCHAR(25)') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1550682763143-3', author: 'rafae (generated)') {
    createTable(tableName: 'person') {
      column(name: 'id', type: 'BIGINT', autoIncrement: true) {
        constraints(primaryKey: true, primaryKeyName: 'personPK')
      }
      column(name: 'name', type: 'VARCHAR(100)') {
        constraints(nullable: false)
      }
      column(name: 'house_id', type: 'BIGINT') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1550682763143-4', author: 'rafae (generated)') {
    addForeignKeyConstraint(baseColumnNames: 'house_id', baseTableName: 'person', constraintName: 'fk_person_house', deferrable: false, initiallyDeferred: false, referencedColumnNames: 'id', referencedTableName: 'house')
  }

}
