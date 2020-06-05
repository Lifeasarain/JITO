rm(list=ls())
require(effsize) 
source("/home/qiufc/R/base.R")
build_model <- function(projectname){

 # rm(list=ls())
 # require(effsize) 
 # source("/home/qiufc/R/base.R")
  root <- "/home/qiufc/Python/JIT_defect_prediction_code/defect_features/report/"

 
  remain_features <- c("commit_id", "time_stamp", "creation_time", "is_fix",
                       "ns", "nd", "nf", "entropy", "la", "ld", "lt", "ndev", "age", "nuc", "exp", "rexp", "sexp", "contains_bug")
  model_features <- c("is_fix", "ns", "nd", "nf", "entropy", "la", "ld", "lt", "ndev", "age", "nuc", "exp", "rexp", "sexp", "contains_bug")

  log_features <- c("ns", "nd", "nf", "entropy", "la", "ld", "lt", "ndev", "age", "nuc", "exp", "rexp", "sexp")
  label <- "contains_bug"
  positive_label <- "TRUE"
  filter_features <- c(label)
  
  # result preparation
  result_frame <- NULL
  
  fn <- paste(c(root, projectname, ".csv"), collapse="")
  
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
  train_size <- size

  train_start <- 1
  train_end <- train_size
  train_data <- data_log[train_start:train_end,]
  train_data <- undersampling(train_data, label)
  if (length(which(train_data$is_fix == '1'))==0){
    model_features2 <- c("ns", "nd", "nf", "entropy", "la", "ld", "lt", "ndev", "age", "nuc", "exp", "rexp", "sexp", "contains_bug")
    var_names1 <- var_names[var_names %in% model_features2]
    var_names_str <- paste(var_names1, collapse="+")
    form <- as.formula(paste(label, var_names_str, sep=" ~ "))
  }

  #GLM
  fit <- glm(form, train_data, family=binomial(link="logit"))
  save(fit, file = "/home/qiufc/R/learnedModel.RData")
}
