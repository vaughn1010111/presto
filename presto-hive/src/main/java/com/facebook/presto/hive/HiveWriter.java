/*
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
package com.facebook.presto.hive;

import com.facebook.presto.spi.Page;
import com.google.common.collect.ImmutableList;

import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;

public class HiveWriter
{
    private final HiveFileWriter fileWriter;
    private final Optional<String> partitionName;
    private final boolean isNew;
    private final String fileName;
    private final String writePath;
    private final String targetPath;

    public HiveWriter(HiveFileWriter fileWriter, Optional<String> partitionName, boolean isNew, String fileName, String writePath, String targetPath)
    {
        this.fileWriter = fileWriter;
        this.partitionName = partitionName;
        this.isNew = isNew;
        this.fileName = fileName;
        this.writePath = writePath;
        this.targetPath = targetPath;
    }

    public long getSystemMemoryUsage()
    {
        return fileWriter.getSystemMemoryUsage();
    }

    public void append(Page dataPage)
    {
        fileWriter.appendRows(dataPage);
    }

    public void commit()
    {
        fileWriter.commit();
    }

    public Optional<Runnable> getVerificationTask()
    {
        return fileWriter.getVerificationTask();
    }

    public void rollback()
    {
        fileWriter.rollback();
    }

    public PartitionUpdate getPartitionUpdate()
    {
        return new PartitionUpdate(
                partitionName.orElse(""),
                isNew,
                writePath,
                targetPath,
                ImmutableList.of(fileName));
    }

    @Override
    public String toString()
    {
        return toStringHelper(this)
                .add("fileWriter", fileWriter)
                .toString();
    }
}
