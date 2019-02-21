package db.changelog

databaseChangeLog {
  changeSet(id: '1550756768496-1', author: 'rafaeljpc') {
    createSequence(sequenceName: 'hibernate_sequence')
  }

  changeSet(id: '1550756768496-2', author: 'rafaeljpc') {
    createTable(tableName: 'house') {
      column(name: 'id', type: 'BIGINT', autoIncrement: true) {
        constraints(primaryKey: true, primaryKeyName: 'housePK')
      }
      column(name: 'name', type: 'VARCHAR(100)') {
        constraints(nullable: false)
      }
    }
  }

  changeSet(id: '1550756768496-3', author: 'rafaeljpc') {
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

  changeSet(id: '1550756768496-4', author: 'rafaeljpc') {
    addForeignKeyConstraint(baseColumnNames: 'house_id', baseTableName: 'person', constraintName: 'fk_person_house', deferrable: false, initiallyDeferred: false, referencedColumnNames: 'id', referencedTableName: 'house')
  }

}