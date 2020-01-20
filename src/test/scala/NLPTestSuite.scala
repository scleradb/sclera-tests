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

import org.scalatest.CancelAfterFailure
import org.scalatest.funspec.AnyFunSpec

import com.scleradb.exec.Processor

import com.scleradb.sql.datatypes.Column
import com.scleradb.sql.result.TableRow

class NLPTestSuite extends AnyFunSpec with CancelAfterFailure {
    var processor: Processor = null

    describe("NLP Query Processing") {
        it("should setup") {
            processor = Processor()
            processor.init()
        }

        it("should execute an NLP query") {
            val query: String =
                "select * from (" +
                    "(select 'My friend George works in London.' as txt)" +
                    " text extract('person', 'LOCATION') in txt" +
                    " to entity" +
                ") as foo"

            processor.handleStatement(query, { ts =>
                val cols: List[Column] = ts.columns
                assert(cols.size === 3)

                assert(cols(0).name === "ENTITY") // specified name
                assert(cols(1).name === "TXT_LABEL") // assigned (default) name
                assert(cols(2).name === "TXT")

                val rows: List[TableRow] = ts.rows.toList
                assert(rows.size === 2)

                assert(
                    rows(0).getStringOpt("TXT") === rows(1).getStringOpt("TXT")
                )

                assert(rows(0).getStringOpt("ENTITY") === Some("George"))
                assert(rows(0).getStringOpt("TXT_LABEL") === Some("PERSON"))
                assert(rows(1).getStringOpt("ENTITY") === Some("London"))
                assert(rows(1).getStringOpt("TXT_LABEL") === Some("LOCATION"))
            })
        }

        it("should teardown") {
            processor.close()
        }
    }
}
