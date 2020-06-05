onecommit_predict <- function(projectname){

    rm(list=ls())
    require(effsize)
    source("/home/qiufc/R/base.R")
    root <- "/home/qiufc/Python/JIT_defect_prediction_code/defect_features/report/"

    #projects <- c("deeplearning4j")
    remain_features <- c("commit_id", "time_stamp", "creation_time", "is_fix",
                     "ns", "nd", "nf", "entropy", "la", "ld", "lt", "ndev", "age", "nuc", "exp", "rexp", "sexp", "contains_bug")
    model_features <- c("is_fix", "ns", "nd", "nf", "entropy", "la", "ld", "lt", "ndev", "age", "nuc", "exp", "rexp", "sexp", "contains_bug")
    log_features <- c("ns", "nd", "nf", "entropy", "la", "ld", "lt", "ndev", "age", "nuc", "exp", "rexp", "sexp")
    label <- "contains_bug"
    positive_label <- "TRUE"
    filter_features <- c(label)

    # result preparation
    result_frame <- NULL
    #p <- "deeplearning4j"
    p <- projectname
    #fn <- paste(c(root,"deeplearning4j_one.csv"), collapse="")
    fn <- paste(c(root,projectname+"_one.csv"), collapse="")
    data <- read.csv(fn)

    temp_label <- tolower(data[label][,1])
    temp_label <- as.vector(temp_label)
    temp_label[which(temp_label=="false")] <- "FALSE"
    temp_label[which(temp_label=="true")] <- "TRUE"
    temp_label_factor <- factor(temp_label, order=TRUE, levels=c("FALSE", "TRUE"))
    data[label] <- temp_label_factor
    data <- data[order(data$time_stamp),] # order by time, increasing order
    var_names <- names(data)
    var_names1 <- var_names[var_names %in% model_features]
    var_names1 <- var_names1[!var_names1 %in% filter_features]
    var_names_str <- paste(var_names1, collapse="+")
    form <- as.formula(paste(label, var_names_str, sep=" ~ "))
    var_names2 <- append(var_names1, label)
    var_names2 <- append("commit_id", var_names2)
    temp_data <- data[var_names2]
    data_log <- get_log_data(temp_data, log_features)
    data_log$real_la <- data$la
    data_log$real_ld <- data$ld

    # result preparation
    precision_scores <- c()
    recall_scores <- c()
    F1_scores <- c()

    # factorise labels
    buggy_labels <- factor(data_log[label][,1], order=TRUE, levels=c("FALSE", "TRUE"))
    data_log[label][,1] <- buggy_labels
    train_data <- NULL


    #fixed ratio validation
    size <- dim(data_log)[1]
    test_start <- 1
    test_end <- size
    test_data <- data_log[test_start:test_end,]

    # GLM
    load("/home/qiufc/R/learnedModel.RData")
    prediction <- predict(fit, test_data, type="response")
    test_data$prob <- prediction
    #test_data$pre_label <- 0
    #test_data$pre_label[which(test_data$prob>0.5)] <- 1
    if (test_data$prob>0.5){
      return ('1')
    }
    else{
      return ('0')
    }
}

#fn_output <- paste(c(root, p,"_one_result.csv"), collapse="")
#write.csv(test_data, fn_output, row.names=FALSE)
