package org.jump.parser;

import org.jump.parser.grammer.*;
import java.util.*;
import org.apache.commons.lang3.StringUtils;

public class Analyzer extends JumpBaseVisitor<Object> {
    @Override
    public Object visitPrimaryStatement(JumpParser.PrimaryStatementContext ctx) {
        ArrayList<Object> items = new ArrayList<Object>();

        for (int i =0; i < ctx.command().size(); i++) {
            Object item = (Object)visit(ctx.command(i));
            items.add(item);
        }

        return items;
    }

    @Override
    public Object visitSqlStatement(JumpParser.SqlStatementContext ctx) {
        ArrayList<String> items = new ArrayList<String>();
        for (int i =0; i < ctx.STRING().size(); i++) {
            items.add(stripQuotes(ctx.STRING(i).getText()));
        }

        SqlCommand cmd = new SqlCommand();
        cmd.setSqls(items);

        return cmd;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object visitInsertStatement(JumpParser.InsertStatementContext ctx) {
        InsertCommand cmd = new InsertCommand();

        cmd.setTableName(ctx.WORD(0).getText());
        String numRowsText = ctx.WORD(1).getText();

        if (!StringUtils.isNumeric(numRowsText)) {
            throw new RuntimeException("2nd Parameter passed to insert(table_name, num_rows) should be integer: Line: "
                + ctx.getStart().getLine()
                + " Col: "
                + ctx.getStart().getCharPositionInLine());
        }
        cmd.setNumRows(Integer.parseInt(ctx.WORD(1).getText()));

        if (ctx.WORD().size() > 2) {
            cmd.setStorageIdentifier(ctx.WORD(2).getText());
        }

        ArrayList<FieldConfig> fields = (ArrayList<FieldConfig>)visit(ctx.input());
        cmd.setFieldConfigs(fields);

        return cmd;
    }

    @Override
    public Object visitRollbackStatement(JumpParser.RollbackStatementContext ctx) {
        AbstractCommand command = new AbstractCommand();
        command.setType(CommandType.ROLLBACK_COMMAND);
        return command;
    }

    @Override
    public Object visitFieldConfigList(JumpParser.FieldConfigListContext ctx) {
        ArrayList<FieldConfig> fclist = new ArrayList<FieldConfig>();
        for (int i =0; i< ctx.field_config().size(); i++) {

            FieldConfig inner = (FieldConfig)visit(ctx.field_config(i));
            fclist.add(inner);
        }

        return fclist;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object visitFieldConfig(JumpParser.FieldConfigContext ctx) {
        FieldConfig fc = new FieldConfig();

        fc.setFieldName(ctx.WORD(0).getText());
        fc.setFnName(ctx.WORD(1).getText());

        ArrayList<String> paramList = (ArrayList<String>)visit(ctx.param_list());
        fc.setParams(paramList);

        return fc;
    }

    @Override
    public Object visitEmptyFieldConfig(JumpParser.EmptyFieldConfigContext ctx) {
        FieldConfig fc = new FieldConfig();

        fc.setFieldName(ctx.WORD(0).getText());
        fc.setFnName(ctx.WORD(1).getText());

        return fc;
    }

    @Override
    public Object visitParamList(JumpParser.ParamListContext ctx) {
        ArrayList<String> items = new ArrayList<String>();
        for (int i=0; i< ctx.item().size(); i++) {
            String inner = (String)visit(ctx.item(i));
            items.add(inner);
        }

        return items;
    }

    @Override
    public Object visitParamItem(JumpParser.ParamItemContext ctx) {
        if (ctx.STRING() != null) {
            String text = ctx.STRING().getText();
            return stripQuotes(text);
        }

        if (ctx.WORD() != null) {
            return (ctx.WORD().getText());
        }

        throw new RuntimeException("Given text is neither word not text");
    }

    private String stripQuotes(String text) {
        return text.substring(1, text.length()-1);
    }
}
