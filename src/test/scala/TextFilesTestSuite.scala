/**
* Sclera - Tests
* Copyright 2012 - 2020 Sclera, Inc.
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
*     http://www.apache.org/licenses/LICENSE-2.0
* 
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.scleradb.test

import java.io.{File, PrintWriter}

import org.scalatest.CancelAfterFailure
import org.scalatest.funspec.AnyFunSpec

import com.scleradb.sql.statements.{SqlStatement, SqlRelQueryStatement}
import com.scleradb.sql.types.SqlCharVarying
import com.scleradb.sql.datatypes.Column
import com.scleradb.sql.result.TableRow

import com.scleradb.exec.Processor

class TextFilesTestSuite extends AnyFunSpec with CancelAfterFailure {
    var processor: Processor = null

    val textFile: File = File.createTempFile("test", "text")
    val textFileName: String = textFile.getCanonicalPath()
    val textContents: String = "foo bar"

    describe("TEXTFILES Query Processing") {
        it("should setup") {
            val text: PrintWriter = new PrintWriter(textFile)
            text.write(textContents)
            text.close()

            processor = Processor()
            processor.init()
        }

        it("should execute a TEXTFILES query (1 file)") {
            val query: String =
                "select * from external textfiles ('" + textFileName + "')"

            val parseResult: List[SqlStatement] =
                processor.parser.parseSqlStatements(query)
            assert(parseResult.size === 1)
            assert(parseResult.head.isInstanceOf[SqlRelQueryStatement] === true)

            val qstmt: SqlRelQueryStatement =
                parseResult.head.asInstanceOf[SqlRelQueryStatement]

            processor.handleQueryStatement(qstmt, { ts =>
                val columns: List[Column] = ts.columns
                assert(columns.size === 2)

                assert(columns(0) === Column("FILE", SqlCharVarying(None)))
                assert(columns(1) === Column("CONTENTS", SqlCharVarying(None)))

                val rows: List[TableRow] = ts.rows.toList
                assert(rows.size === 1)

                assert(rows(0).getStringOpt("FILE") === Some(textFileName))
                assert(rows(0).getStringOpt("CONTENTS") === Some(textContents))
            })
        }

        it("should teardown") {
            textFile.delete()
            processor.close()
        }
    }
}
