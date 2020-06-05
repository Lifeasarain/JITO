
root <- "/home/qiufc/Python/JIT_defect_prediction_code/defect_features/report/"
build_model <- function(projectname){
    fn <- paste(c(root, projectname, ".csv"), collapse="")
    return(fn)
}
