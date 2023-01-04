package org.jjche.cloud.rest;

import org.jjche.core.annotation.controller.ApiRestController;
import org.jjche.minio.rest.MinioBaseController;
import org.jjche.minio.util.MinioUtil;


/**
 * <p>
 * Minio 控制器
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-02-02
 */
@ApiRestController
public class CloudMinioController extends MinioBaseController {

    public CloudMinioController(MinioUtil minioUtil) {
        super(minioUtil);
    }

}